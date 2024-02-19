package com.ead.course.controllers;

import com.ead.course.clients.CourseClient;
import com.ead.course.dtos.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    private final CourseClient courseClient;

    @Autowired
    public CourseUserController(final CourseClient courseClient) {
        this.courseClient = courseClient;
    }

    @GetMapping(value = "/courses/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUsersByCourse(
            @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC)
            final Pageable pageable,
            @PathVariable(value = "courseId") final UUID courseId
    ) {
        final Page<UserDTO> allCoursesByUser = this.courseClient
                .getAllUsersByCourse(courseId, pageable);

        final ResponseEntity<Page<UserDTO>> usersByCourseResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(allCoursesByUser);

        return usersByCourseResponse;
    }

}
