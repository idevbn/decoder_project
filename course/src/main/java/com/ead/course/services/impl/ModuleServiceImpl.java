package com.ead.course.services.impl;

import com.ead.course.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl implements ModuleService {

    private final ModuleService moduleService;

    @Autowired
    public ModuleServiceImpl(final ModuleService moduleService) {
        this.moduleService = moduleService;
    }

}
