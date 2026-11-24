package com.recoia.catalogservice.dto;

import com.recoia.catalogservice.model.enums.CourseSkillType;

public record CourseSkillRequest(Long skillId, CourseSkillType type) {}
