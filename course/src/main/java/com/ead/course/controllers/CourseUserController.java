package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    private final AuthUserClient authUserClient;
    private final CourseService courseService;
    private final CourseUserService courseUserService;

    @Autowired
    public CourseUserController(final AuthUserClient authUserClient,
                                final CourseService courseService,
                                final CourseUserService courseUserService) {
        this.authUserClient = authUserClient;
        this.courseService = courseService;
        this.courseUserService = courseUserService;
    }

    @GetMapping(value = "/courses/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUsersByCourse(
            @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC)
            final Pageable pageable,
            @PathVariable(value = "courseId") final UUID courseId
    ) {
        final Page<UserDTO> allCoursesByUser = this.authUserClient
                .getAllUsersByCourse(courseId, pageable);

        final ResponseEntity<Page<UserDTO>> usersByCourseResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(allCoursesByUser);

        return usersByCourseResponse;
    }

    @PostMapping(value = "/courses/{courseId}/users/subscription")
    public ResponseEntity<?> saveSubscriptionUserInCourse(
            @PathVariable(value = "courseId") final UUID courseId,
            @RequestBody @Valid final SubscriptionDTO subscriptionDTO
    ) {
        ResponseEntity<UserDTO> responseUser;

        final Optional<CourseModel> optionalCourseModel = this.courseService.findById(courseId);

        if (optionalCourseModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final boolean existsByCourseAndUserId = this.courseUserService
                .existsByCourseAndUserId(optionalCourseModel.get(), subscriptionDTO.getUserId());

        if (existsByCourseAndUserId) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        try {
            responseUser = this.authUserClient.getOneUserById(subscriptionDTO.getUserId());

            if (responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (final HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        }

        final CourseUserModel courseUserModel = this.courseUserService
                .save(optionalCourseModel.get().convertToCourseUserModel(subscriptionDTO.getUserId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }

}
