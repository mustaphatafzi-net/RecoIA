package com.recoia.catalogservice.repository;

import com.recoia.catalogservice.model.CourseSkill;
import com.recoia.catalogservice.model.enums.CourseSkillType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseSkillRepository extends JpaRepository<CourseSkill, Long> {

    // toutes les compétences liées à un cours
    List<CourseSkill> findByCourseId(Long courseId);

    // sert à empêcher les doublons
    boolean existsByCourseIdAndSkillIdAndType(Long courseId, Long skillId, CourseSkillType type);
}
