package com.ead.course.controllers;

import com.ead.course.dtos.LessonDTO;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    private final LessonService lessonService;
    private final ModuleService moduleService;

    @Autowired
    public LessonController(final LessonService lessonService, final ModuleService moduleService) {
        this.lessonService = lessonService;
        this.moduleService = moduleService;
    }

    @PostMapping(value = "/modules/{moduleId}/lessons")
    public ResponseEntity<LessonModel> saveLesson(
            @RequestBody final LessonDTO lessonDTO,
            @PathVariable(value = "moduleId") final UUID moduleId
    ) {
        final Optional<ModuleModel> optionalModuleModel = this.moduleService.findById(moduleId);

        if (!optionalModuleModel.isPresent()) {
            final ResponseEntity<LessonModel> lessonResponse
                    = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return lessonResponse;
        }

        var lessonModel = new LessonModel();

        BeanUtils.copyProperties(lessonDTO, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(optionalModuleModel.get());

        final LessonModel savedLessonModel = this.lessonService.save(lessonModel);

        final ResponseEntity<LessonModel> lessonResponse = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedLessonModel);

        return lessonResponse;
    }

    @DeleteMapping(value = "/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<LessonModel> deleteLesson(
            @PathVariable(value = "moduleId") final UUID moduleId,
            @PathVariable(value = "lessonId") final UUID lessonId
    ) {
        final Optional<LessonModel> optionalLessonModel
                = this.lessonService.findLessonIntoModule(moduleId, lessonId);

        if (!optionalLessonModel.isPresent()) {
            final ResponseEntity<LessonModel> lessonResponse
                    = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return lessonResponse;
        }
        this.lessonService.delete(optionalLessonModel.get());

        final ResponseEntity<LessonModel> lessonResponse = ResponseEntity
                .status(HttpStatus.OK)
                .build();

        return lessonResponse;
    }

    @PutMapping(value = "/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<LessonModel> updateLesson(
            @PathVariable(value = "moduleId") final UUID moduleId,
            @PathVariable(value = "lessonId") final UUID lessonId,
            @RequestBody @Valid final LessonDTO lessonDTO
    ) {
        final Optional<LessonModel> optionalLessonModelModel
                = this.lessonService.findLessonIntoModule(moduleId, lessonId);

        if (!optionalLessonModelModel.isPresent()) {
            final ResponseEntity<LessonModel> lessonModule
                    = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return lessonModule;
        }
        var lessonModel = optionalLessonModelModel.get();

        BeanUtils.copyProperties(lessonDTO, lessonModel, "id", "creationDate");

        final LessonModel savedLessonModel = this.lessonService.save(lessonModel);

        final ResponseEntity<LessonModel> lessonReponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(savedLessonModel);

        return lessonReponse;
    }

    @GetMapping(value = "/modules/{moduleId}/lessons")
    public ResponseEntity<List<LessonModel>> getAllLessons(
            @PathVariable(value = "moduleId") final UUID moduleId
    ) {
        final List<LessonModel> lessons = this.lessonService.findAllByModule(moduleId);

        final ResponseEntity<List<LessonModel>> lessonsResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(lessons);

        return lessonsResponse;
    }

    @GetMapping(value = "/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<LessonModel> getOneLesson(
            @PathVariable(value = "moduleId") final UUID moduleId,
            @PathVariable(value = "lessonId") final UUID lessonId
    ) {
        final Optional<LessonModel> optionalLessonModel
                = this.lessonService.findLessonIntoModule(moduleId, lessonId);

        if (!optionalLessonModel.isPresent()) {
            final ResponseEntity<LessonModel> lessonResponse = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

            return lessonResponse;
        }

        final ResponseEntity<LessonModel> lessonResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(optionalLessonModel.get());

        return lessonResponse;
    }

}
