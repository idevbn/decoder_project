package com.ead.authuser.controllers;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = this.userService.findAll();

        ResponseEntity<List<UserModel>> usersResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(users);

        return usersResponse;
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserModel> getOneUser(
            @PathVariable(name = "userId") UUID userId
    ) {
        Optional<UserModel> userModelOptional = this.userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            ResponseEntity<UserModel> userResponse = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
            
            return userResponse;
        }

        ResponseEntity<UserModel> userResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(userModelOptional.get());

        return userResponse;
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<UserModel> deleteUser(
            @PathVariable(name = "userId") UUID userId
    ) {
        Optional<UserModel> userModelOptional = this.userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            ResponseEntity<UserModel> userResponse = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

            return userResponse;
        }

        this.userService.delete(userModelOptional.get());

        ResponseEntity<UserModel> userResponse = ResponseEntity
                .status(HttpStatus.OK)
                .build();

        return userResponse;
    }
}
