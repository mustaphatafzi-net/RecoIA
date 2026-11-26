package com.recoia.studentservice.dto;

import com.recoia.studentservice.model.enums.AcademicLevel;

import java.util.List;
import java.util.Set;

// Ce que l'API renvoie
public record StudentResponse(
        Long id,
        Long userId,
        AcademicLevel academicLevel,
        String professionalGoal,
        Set<Long> categoryIds,
        List<StudentSkillDto> skills
) {
}
