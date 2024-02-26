package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping
public class UserCourseController {

    private final CourseClient courseClient;
    private final UserService userService;
    private final UserCourseService userCourseService;

    @Autowired
    public UserCourseController(final CourseClient courseClient,
                                final UserService userService,
                                final UserCourseService userCourseService) {
        this.courseClient = courseClient;
        this.userService = userService;
        this.userCourseService = userCourseService;
    }

    /**
     * Traz todos os cursos de um determinado usuário.
     *
     * Utiliza o API Composition Pattern para a comunicação entre
     * os microsserviços authuser e course
     *
     * @param pageable -> paginação
     * @param userId -> identificador do usuário
     * @return {@link CourseDTO} com os cursos do usuário
     */
    @GetMapping(value = "/users/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllCoursesByUser(
            @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC)
            final Pageable pageable,
            @PathVariable(value = "userId") final UUID userId
    ) {
        final Page<CourseDTO> allCoursesByUser = this.courseClient.getAllCoursesByUser(userId, pageable);

        final ResponseEntity<Page<CourseDTO>> coursesByUserResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(allCoursesByUser);

        return coursesByUserResponse;
    }

    @PostMapping(value = "/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "userId") final UUID userId,
            @RequestBody @Valid final UserCourseDTO userCourseDTO
    ) {
        final Optional<UserModel> userModelOptional = this.userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final boolean existsByUserAndCourseId = this.userCourseService
                .existsByUserAndCourseId(userModelOptional.get(), userCourseDTO.getCourseId());

        if (existsByUserAndCourseId) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        final UserCourseModel userCourseModel = this.userCourseService
                .save(userModelOptional.get().convertToUserCourseModel(userCourseDTO.getCourseId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
    }

    @DeleteMapping("/users/courses/{courseId}")
    public ResponseEntity<Object> deleteUserCourseByCourse(
            @PathVariable(value = "courseId") final UUID courseId
    ) {
        if (!this.userCourseService.existsByCourseId(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "UserCourse not found.");
        }

        this.userCourseService.deleteUserCourseByCourse(courseId);

        return ResponseEntity.status(HttpStatus.OK).body("UserCourse deleted successfully.");
    }

}
