package com.ead.course.services.impl;

import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseService courseService;

    @Autowired
    public CourseServiceImpl(final CourseService courseService) {
        this.courseService = courseService;
    }

}
