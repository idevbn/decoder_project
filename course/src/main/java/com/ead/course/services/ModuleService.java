package com.ead.course.services;

import com.ead.course.models.ModuleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {

    void delete(final ModuleModel moduleModel);

    ModuleModel save(final ModuleModel moduleModel);

    Optional<ModuleModel> findModuleIntoCourse(final UUID courseId, final UUID moduleId);

    List<ModuleModel> findAllByCourse(final UUID courseId);

    Optional<ModuleModel> findById(final UUID moduleId);

}
