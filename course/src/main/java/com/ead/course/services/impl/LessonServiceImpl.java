package com.ead.course.services.impl;

import com.ead.course.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonService lessonService;

    @Autowired
    public LessonServiceImpl(final LessonService lessonService) {
        this.lessonService = lessonService;
    }

}
