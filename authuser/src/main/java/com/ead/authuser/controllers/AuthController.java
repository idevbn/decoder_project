package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
@RestController
@RequestMapping(value = "/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Object> registerUser(
            @RequestBody
            @Validated(UserDTO.UserView.RegistrationPost.class)
            @JsonView(UserDTO.UserView.RegistrationPost.class) final UserDTO userDTO
            ) {
        log.debug("POST registerUser userDTO received {}", userDTO.toString());

        if (this.userService.existsByUserName(userDTO.getUsername())) {
            log.warn("Username {} is already taken!", userDTO.getUsername());

            final ResponseEntity<Object> userResponse = ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Error: Username is already taken!");

            return userResponse;
        }

        if (this.userService.existsByEmail(userDTO.getEmail())) {
            log.warn("Email {} is already taken!", userDTO.getEmail());

            final ResponseEntity<Object> userResponse = ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Error: Email is already taken!");

            return userResponse;
        }

        var userModel = new UserModel();

        BeanUtils.copyProperties(userDTO, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        this.userService.save(userModel);

        final ResponseEntity<Object> userResponse = ResponseEntity.
                status(HttpStatus.CREATED)
                .body(userModel);

        log.debug("POST registerUser userModel userId {}", userModel.getUserId());
        log.info("User saved successfully userId {}", userModel.getUserId());

        return userResponse;
    }

}
