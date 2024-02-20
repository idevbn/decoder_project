package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;
    private final AuthUserClient authUserClient;

    @Autowired
    public CourseUserServiceImpl(final CourseUserRepository courseUserRepository,
                                 final AuthUserClient authUserClient) {
        this.courseUserRepository = courseUserRepository;
        this.authUserClient = authUserClient;
    }

    @Override
    public boolean existsByCourseAndUserId(final CourseModel courseModel, final UUID userId) {
        return this.courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourseUserModel save(final CourseUserModel courseUserModel) {
        return this.courseUserRepository.save(courseUserModel);
    }

    @Override
    @Transactional
    public CourseUserModel saveAndSendSubscriptionUserInCourse(final CourseUserModel courseUserModel) {
        final CourseUserModel savedCourseUserModel = this.courseUserRepository.save(courseUserModel);

        this.authUserClient.postSubscriptionUserInCourse(
                savedCourseUserModel.getCourse().getCourseId(),
                savedCourseUserModel.getUserId()
        );

        return savedCourseUserModel;
    }

}
