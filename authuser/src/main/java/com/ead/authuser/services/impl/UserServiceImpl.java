package com.ead.authuser.services.impl;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserModel> findAll() {
        final List<UserModel> users = this.userRepository.findAll();

        return users;
    }

    @Override
    public Optional<UserModel> findById(final UUID id) {
        final Optional<UserModel> userModelOptional = this.userRepository.findById(id);

        return userModelOptional;
    }

    @Override
    public void delete(final UserModel userModel) {
        this.userRepository.delete(userModel);
    }

    @Override
    public UserModel save(final UserModel userModel) {
        final UserModel user = this.userRepository.save(userModel);

        return user;
    }

    @Override
    public boolean existsByUserName(final String username) {
        final boolean existsUsername = this.userRepository.existsByUsername(username);

        return existsUsername;
    }

    @Override
    public boolean existsByEmail(final String email) {
        final boolean existsEmail = this.userRepository.existsByEmail(email);

        return existsEmail;
    }

    @Override
    public Page<UserModel> findAll(final Specification<UserModel> spec, final Pageable pageable) {
        final Page<UserModel> userModelPage = this.userRepository.findAll(spec, pageable);

        return userModelPage;
    }

}
