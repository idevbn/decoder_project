package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonModel, UUID>,
        JpaSpecificationExecutor<LessonModel> {

    @Query(value = "SELECT * FROM tb_lessons WHERE module_id = :id", nativeQuery = true)
    List<LessonModel> findAllLessonsIntoModule(@Param("id") final UUID id);

    @Query(value = "SELECT * FROM tb_lessons WHERE module_id = :moduleId AND id = :lessonId", nativeQuery = true)
    Optional<LessonModel> findLessonIntoModule(
            @Param("moduleId") final UUID moduleId,
            @Param("lessonId") final UUID lessonId
    );

}
