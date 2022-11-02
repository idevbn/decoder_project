package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserModel> findAll();

    Optional<UserModel> findById(UUID id);

    void delete(UserModel userModel);

    UserModel save(UserModel userModel);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

}
