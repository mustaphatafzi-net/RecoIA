package com.recoia.catalogservice.dto;

import com.recoia.catalogservice.model.enums.CourseSkillType;

public record CourseSkillResponse(
        Long id,
        Long courseId,
        Long skillId,
        String skillName,
        CourseSkillType type
) {}
