package com.recoia.recommendationservice.client.dto;

/** Une competence d'un cours (GET /courses/{id}/skills) : type = PREREQUISITE ou LEARNING_OUTCOME. */
public record CourseSkillData(Long id, Long courseId, Long skillId, String skillName, String type) {
}
