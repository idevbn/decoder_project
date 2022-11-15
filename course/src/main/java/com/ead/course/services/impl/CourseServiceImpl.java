package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public CourseServiceImpl(
            final CourseRepository courseRepository,
            final ModuleRepository moduleRepository,
            final LessonRepository lessonRepository
    ) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
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
        final List<ModuleModel> moduleModelList
                = this.moduleRepository.findAllModulesIntoCourse(courseModel.getId());

        if (!moduleModelList.isEmpty()) {
            for (ModuleModel module : moduleModelList) {
                final List<LessonModel> lessonModelList
                        = this.lessonRepository.findAllLessonsIntoModule(module.getId());

                if (!lessonModelList.isEmpty()) {
                    this.lessonRepository.deleteAll(lessonModelList);
                }
            }

            this.moduleRepository.deleteAll(moduleModelList);
        }

        this.courseRepository.delete(courseModel);
    }

}
