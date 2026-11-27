package com.recoia.recommendationservice.dto;

/** Ligne brute du classement : "le cours 12 a ete recommande 9 fois". */
public record CourseCountResponse(Long courseId, Long total) {
}
