package com.ead.authuser.services;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;

import java.util.UUID;

public interface UserCourseService {
    boolean existsByUserAndCourseId(final UserModel userModel, final UUID courseId);

    UserCourseModel save(final UserCourseModel userCourseModel);
}
