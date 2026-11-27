package com.recoia.interactionservice.dto;

/** Les chiffres d'un cours : note moyenne, nombre de notes, de favoris et de consultations. */
public record CourseStatsResponse(Long courseId, Double averageRating, Long ratingCount,
                                  Long favoriteCount, Long viewCount) {
}
