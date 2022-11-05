package com.ead.authuser.controllers;

import com.ead.authuser.controllers.dtos.UserDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(
            final SpecificationTemplate.UserSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            final Pageable pageable
            ) {
        final Page<UserModel> userModelPage = this.userService.findAll(spec, pageable);

        if (!userModelPage.isEmpty()) {
            for (UserModel user : userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getId())).withSelfRel());
            }
        }

        final ResponseEntity<Page<UserModel>> usersResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(userModelPage);

        return usersResponse;
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserModel> getOneUser(
            @PathVariable(name = "userId") final UUID userId
    ) {
        Optional<UserModel> userModelOptional = this.userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            final ResponseEntity<UserModel> userResponse = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
            
            return userResponse;
        }

        final ResponseEntity<UserModel> userResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(userModelOptional.get());

        return userResponse;
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<UserModel> deleteUser(
            @PathVariable(name = "userId") final UUID userId
    ) {
        Optional<UserModel> userModelOptional = this.userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            final ResponseEntity<UserModel> userResponse = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

            return userResponse;
        }

        this.userService.delete(userModelOptional.get());

        final ResponseEntity<UserModel> userResponse = ResponseEntity
                .status(HttpStatus.OK)
                .build();

        return userResponse;
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<UserModel> updateUser(
            @PathVariable(name = "userId") final UUID userId,
            @RequestBody
            @Validated(UserDTO.UserView.UserPut.class)
            @JsonView(UserDTO.UserView.UserPut.class) final UserDTO userDTO
    ) {
        Optional<UserModel> userModelOptional = this.userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            final ResponseEntity<UserModel> userResponse = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

            return userResponse;
        }

        final UserModel userModel = userModelOptional.get();
        userModel.setFullName(userDTO.getFullName());
        userModel.setPhoneNumber(userDTO.getPhoneNumber());
        userModel.setCpf(userDTO.getCpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        this.userService.save(userModel);

        final ResponseEntity<UserModel> userResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(userModel);

        return userResponse;
    }

    @PutMapping(value = "/{userId}/password")
    public ResponseEntity<UserModel> updatePassword(
            @PathVariable(name = "userId") final UUID userId,
            @RequestBody
            @Validated(UserDTO.UserView.PasswordPut.class)
            @JsonView(UserDTO.UserView.PasswordPut.class) final UserDTO userDTO
    ) {
        Optional<UserModel> userModelOptional = this.userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            final ResponseEntity<UserModel> userResponse = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

            return userResponse;
        }

        if (userModelOptional.get().getPassword().equals(userDTO.getPassword())) {
            final ResponseEntity<UserModel> userResponse = ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();

            return userResponse;
        }

        final UserModel userModel = userModelOptional.get();
        userModel.setPassword(userDTO.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        this.userService.save(userModel);

        final ResponseEntity<UserModel> userResponse = ResponseEntity
                .status(HttpStatus.OK)
                .build();

        return userResponse;
    }

    @PutMapping(value = "/{userId}/image")
    public ResponseEntity<UserModel> updateImage(
            @PathVariable(name = "userId") final UUID userId,
            @RequestBody
            @Validated(UserDTO.UserView.ImagePut.class)
            @JsonView(UserDTO.UserView.ImagePut.class) final UserDTO userDTO
    ) {
        Optional<UserModel> userModelOptional = this.userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            ResponseEntity<UserModel> userResponse = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

            return userResponse;
        }

        UserModel userModel = userModelOptional.get();
        userModel.setImageUrl(userDTO.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        this.userService.save(userModel);

        final ResponseEntity<UserModel> userResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(userModel);

        return userResponse;
    }

}
