package com.recoia.recommendationservice.client.dto;

/** Un cours tel que le renvoie catalog-service (GET /courses). */
public record CourseData(Long id, String title, String description, String imageUrl, String level,
                         Integer durationHours, String provider, String format, String language,
                         Double averageRating, Integer ratingCount, Boolean active,
                         Long categoryId, String categoryName) {
}
