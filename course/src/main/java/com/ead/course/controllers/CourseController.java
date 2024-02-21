package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validation.CourseValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    private final CourseService courseService;
    private final CourseValidator courseValidator;

    @Autowired
    public CourseController(final CourseService courseService,
                            final CourseValidator courseValidator) {
        this.courseService = courseService;
        this.courseValidator = courseValidator;
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody final CourseDTO courseDTO,
                                                  final Errors errors) {
        var courseModel = new CourseModel();

        this.courseValidator.validate(courseDTO, errors);

        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }

        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        final CourseModel savedCourseModel = this.courseService.save(courseModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourseModel);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable(value = "id") final UUID id) {
        final Optional<CourseModel> optionalCourseModel = this.courseService.findById(id);

        if (!optionalCourseModel.isPresent()) {
            final ResponseEntity<Void> courseResponse
                    = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return courseResponse;
        }

        this.courseService.delete(optionalCourseModel.get());

        final ResponseEntity<Void> courseReponse = ResponseEntity.status(HttpStatus.OK).build();

        return courseReponse;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CourseModel> updateCourse(
            @PathVariable(value = "id") final UUID id,
            @RequestBody @Valid final CourseDTO courseDTO
    ) {
        final Optional<CourseModel> optionalCourseModel = this.courseService.findById(id);

        if (!optionalCourseModel.isPresent()) {
            final ResponseEntity<CourseModel> courseResponse
                    = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return courseResponse;
        }
        var courseModel = optionalCourseModel.get();

        BeanUtils.copyProperties(courseDTO, courseModel, "id", "creationDate");

        final CourseModel savedCourseModel = this.courseService.save(courseModel);

        final ResponseEntity<CourseModel> courseReponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(savedCourseModel);

        return courseReponse;
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(
            final SpecificationTemplate.CourseSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC)
            final Pageable pageable,
            @RequestParam(required = false) final UUID userId
    ) {
        Page<CourseModel> courseModelPagel = null;

        if (userId != null) {
            courseModelPagel = this.courseService
                    .findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable);
        } else {
            courseModelPagel = this.courseService.findAll(spec, pageable);
        }

        final ResponseEntity<Page<CourseModel>> coursesResponse
                = ResponseEntity.status(HttpStatus.OK).body(courseModelPagel);

        return coursesResponse;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CourseModel> getOneCourse(@PathVariable(value = "id") final UUID id) {
        final Optional<CourseModel> optionalCourseModel = this.courseService.findById(id);

        if (!optionalCourseModel.isPresent()) {
            final ResponseEntity<CourseModel> courseResponse
                    = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return courseResponse;
        }

        final ResponseEntity<CourseModel> courseResponse
                = ResponseEntity.status(HttpStatus.OK).body(optionalCourseModel.get());

        return courseResponse;
    }

}
