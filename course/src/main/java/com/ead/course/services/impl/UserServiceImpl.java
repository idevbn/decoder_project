package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements UserService {

    private final UserRepository courseUserRepository;
    private final AuthUserClient authUserClient;

    @Autowired
    public CourseUserServiceImpl(final UserRepository courseUserRepository,
                                 final AuthUserClient authUserClient) {
        this.courseUserRepository = courseUserRepository;
        this.authUserClient = authUserClient;
    }

    @Override
    public boolean existsByCourseAndUserId(final CourseModel courseModel, final UUID userId) {
        return this.courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public UserModel save(final UserModel courseUserModel) {
        return this.courseUserRepository.save(courseUserModel);
    }

    @Override
    @Transactional
    public UserModel saveAndSendSubscriptionUserInCourse(final UserModel courseUserModel) {
        final UserModel savedCourseUserModel = this.courseUserRepository.save(courseUserModel);

        this.authUserClient.postSubscriptionUserInCourse(
                savedCourseUserModel.getCourse().getCourseId(),
                savedCourseUserModel.getUserId()
        );

        return savedCourseUserModel;
    }

    @Override
    public boolean existsByUserId(final UUID userId) {
        return this.courseUserRepository.existsByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteCourseUserByUser(final UUID userId) {
        this.courseUserRepository.deleteAllByUserId(userId);
    }

}
