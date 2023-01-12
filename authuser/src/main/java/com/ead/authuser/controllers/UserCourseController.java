package com.ead.authuser.controllers;

import com.ead.authuser.clients.UserClient;
import com.ead.authuser.controllers.dtos.CourseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping
public class UserCourseController {

    private final UserClient userClient;

    @Autowired
    public UserCourseController(final UserClient userClient) {
        this.userClient = userClient;
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
        final Page<CourseDTO> allCoursesByUser = this.userClient.getAllCoursesByUser(userId, pageable);

        final ResponseEntity<Page<CourseDTO>> coursesByUserResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(allCoursesByUser);

        return coursesByUserResponse;
    }

}
