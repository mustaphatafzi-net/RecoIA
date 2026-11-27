package com.recoia.recommendationservice.dto;

/** Ligne de classement avec le titre du cours, pour le tableau de bord. */
public record TopCourseResponse(Long courseId, String title, Long total) {
}
