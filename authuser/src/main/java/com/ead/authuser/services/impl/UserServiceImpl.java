package com.ead.authuser.services.impl;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserModel> findAll() {
        List<UserModel> users = this.userRepository.findAll();

        return users;
    }

    @Override
    public Optional<UserModel> findById(UUID id) {
        Optional<UserModel> userModelOptional = this.userRepository.findById(id);

        return userModelOptional;
    }

    @Override
    public void delete(UserModel userModel) {
        this.userRepository.delete(userModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        UserModel user = this.userRepository.save(userModel);

        return user;
    }

    @Override
    public boolean existsByUserName(String username) {
        boolean existsUsername = this.userRepository.existsByUsername(username);

        return existsUsername;
    }

    @Override
    public boolean existsByEmail(String email) {
        boolean existsEmail = this.userRepository.existsByEmail(email);

        return existsEmail;
    }

}
