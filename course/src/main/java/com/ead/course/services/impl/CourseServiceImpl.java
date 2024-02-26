package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final CourseUserRepository courseUserRepository;
    private final AuthUserClient authUserClient;

    @Autowired
    public CourseServiceImpl(
            final CourseRepository courseRepository,
            final ModuleRepository moduleRepository,
            final LessonRepository lessonRepository,
            final CourseUserRepository courseUserRepository,
            final AuthUserClient authUserClient
    ) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.courseUserRepository = courseUserRepository;
        this.authUserClient = authUserClient;
    }

    /**
     * Implementando um método de deleção em cascata.
     *
     * É passado o
     * @param courseModel
     * e verificados os módulos associados a esse curso.
     *
     * Caso existam módulos, será feita uma nova verificação,
     * buscando as lições associadas a tais módulos.
     *
     * Caso existam as lições, elas serão excluídas.
     * Depois serão excluídos os módulos e, por fim,
     * será excluído o curso associado.
     */
    @Override
    @Transactional
    public void delete(final CourseModel courseModel) {
        boolean deleteCourseUserInAuthUser = false;

        final List<ModuleModel> moduleModelList
                = this.moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());

        if (!moduleModelList.isEmpty()) {
            for (ModuleModel module : moduleModelList) {
                final List<LessonModel> lessonModelList
                        = this.lessonRepository.findAllLessonsIntoModule(module.getModuleId());

                if (!lessonModelList.isEmpty()) {
                    this.lessonRepository.deleteAll(lessonModelList);
                }
            }

            this.moduleRepository.deleteAll(moduleModelList);
        }

        final List<CourseUserModel> courseUserModelList = this.courseUserRepository
                .findAllCourseUserIntoCourse(courseModel.getCourseId());

        if (!courseUserModelList.isEmpty()) {
            this.courseUserRepository.deleteAll(courseUserModelList);
            deleteCourseUserInAuthUser = true;
        }

        this.courseRepository.delete(courseModel);

        if (deleteCourseUserInAuthUser) {
            this.authUserClient.deleteCourseInAuthUser(courseModel.getCourseId());
        }
    }

    @Override
    public CourseModel save(final CourseModel courseModel) {
        final CourseModel savedCourseModel = this.courseRepository.save(courseModel);

        return savedCourseModel;
    }

    @Override
    public Optional<CourseModel> findById(final UUID id) {
        final Optional<CourseModel> optionalCourseModel = this.courseRepository.findById(id);

        return optionalCourseModel;
    }

    @Override
    public List<CourseModel> findAll() {
        final List<CourseModel> courses = this.courseRepository.findAll();

        return courses;
    }

    @Override
    public Page<CourseModel> findAll(
            final Specification<CourseModel> spec,
            final Pageable pageable
    ) {
        final Page<CourseModel> courseModelPage = this.courseRepository.findAll(spec, pageable);

        return courseModelPage;
    }

}
