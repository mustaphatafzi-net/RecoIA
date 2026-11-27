package com.recoia.recommendationservice.client.dto;

import java.util.List;
import java.util.Set;

/** Le profil d'un etudiant tel que le renvoie student-service (GET /students/me). */
public record StudentData(Long id, Long userId, String academicLevel, String professionalGoal,
                          Set<Long> categoryIds, List<StudentSkillData> skills) {
}
