package com.ead.course.services;

import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;

import java.util.UUID;

public interface CourseUserService {

    boolean existsByCourseAndUserId(final CourseModel courseModel, final UUID userId);

    UserModel save(final UserModel courseUserModel);

    UserModel saveAndSendSubscriptionUserInCourse(final UserModel courseUserModel);

    boolean existsByUserId(final UUID userId);

    void deleteCourseUserByUser(final UUID userId);
}
