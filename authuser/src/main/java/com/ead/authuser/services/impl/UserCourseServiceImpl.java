package com.ead.authuser.services.impl;

import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository userCourseRepository;

    @Autowired
    public UserCourseServiceImpl(final UserCourseRepository userCourseRepository) {
        this.userCourseRepository = userCourseRepository;
    }

}
