package com.ead.authuser.services.impl;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository userCourseRepository;

    @Autowired
    public UserCourseServiceImpl(final UserCourseRepository userCourseRepository) {
        this.userCourseRepository = userCourseRepository;
    }

    @Override
    public boolean existsByUserAndCourseId(final UserModel userModel, final UUID courseId) {
        return this.userCourseRepository.existsByUserAndCourseId(userModel, courseId);
    }

    @Override
    public UserCourseModel save(final UserCourseModel userCourseModel) {
        return this.userCourseRepository.save(userCourseModel);
    }

}
