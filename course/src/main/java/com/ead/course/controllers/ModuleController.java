package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    private final ModuleService moduleService;
    private final CourseService courseService;

    @Autowired
    public ModuleController(final ModuleService moduleService, final CourseService courseService) {
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @PostMapping(value = "/courses/{courseId}/modules")
    public ResponseEntity<ModuleModel> saveModule(
            @RequestBody final ModuleDTO moduleDTO,
            @PathVariable(value = "courseId") final UUID courseId
    ) {
        final Optional<CourseModel> optionalCourseModel = this.courseService.findById(courseId);

        if (!optionalCourseModel.isPresent()) {
            final ResponseEntity<ModuleModel> moduleResponse
                    = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return moduleResponse;
        }

        var moduleModel = new ModuleModel();

        BeanUtils.copyProperties(moduleDTO, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(optionalCourseModel.get());

        final ModuleModel savedModuleModel = this.moduleService.save(moduleModel);

        final ResponseEntity<ModuleModel> moduleResponse = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedModuleModel);

        return moduleResponse;
    }

    @DeleteMapping(value = "/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<ModuleModel> deleteModule(
            @PathVariable(value = "courseId") final UUID courseId,
            @PathVariable(value = "moduleId") final UUID moduleId
    ) {
        final Optional<ModuleModel> optionalModuleModel
                = this.moduleService.findModuleIntoCourse(courseId, moduleId);

        if (!optionalModuleModel.isPresent()) {
            final ResponseEntity<ModuleModel> moduleResponse
                    = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return moduleResponse;
        }
        this.moduleService.delete(optionalModuleModel.get());

        final ResponseEntity<ModuleModel> moduleResponse = ResponseEntity
                .status(HttpStatus.OK)
                .build();

        return moduleResponse;
    }

    @PutMapping(value = "/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<ModuleModel> updateModule(
            @PathVariable(value = "courseId") final UUID courseId,
            @PathVariable(value = "moduleId") final UUID moduleId,
            @RequestBody @Valid final ModuleDTO moduleDTO
    ) {
        final Optional<ModuleModel> optionalModuleModel
                = this.moduleService.findModuleIntoCourse(courseId, moduleId);

        if (!optionalModuleModel.isPresent()) {
            final ResponseEntity<ModuleModel> moduleReponse
                    = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return moduleReponse;
        }
        var moduleModel = optionalModuleModel.get();

        BeanUtils.copyProperties(moduleDTO, moduleModel, "id", "creationDate");

        final ModuleModel savedModuleModel = this.moduleService.save(moduleModel);

        final ResponseEntity<ModuleModel> moduleReponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(savedModuleModel);

        return moduleReponse;
    }

    @GetMapping(value = "/courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModules(
            @PathVariable(value = "courseId") final UUID courseId,
            final SpecificationTemplate.ModuleSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC)
            final Pageable pageable
    ) {
        final Page<ModuleModel> modules = this.moduleService
                .findAllByCourse(SpecificationTemplate.moduleIdCourse(courseId).and(spec), pageable);

        final ResponseEntity<Page<ModuleModel>> moduleResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(modules);

        return moduleResponse;
    }

    @GetMapping(value = "/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<ModuleModel> getOneModule(
            @PathVariable(value = "courseId") final UUID courseId,
            @PathVariable(value = "moduleId") final UUID moduleId
    ) {
        final Optional<ModuleModel> optionalModuleModel
                = this.moduleService.findModuleIntoCourse(courseId, moduleId);

        if (!optionalModuleModel.isPresent()) {
            final ResponseEntity<ModuleModel> moduleResponse = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

            return moduleResponse;
        }

        final ResponseEntity<ModuleModel> moduleResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(optionalModuleModel.get());

        return moduleResponse;
    }

}
