package com.recoia.catalogservice.dto;

import com.recoia.catalogservice.model.enums.CourseFormat;
import com.recoia.catalogservice.model.enums.CourseLevel;

public record CourseResponse(
        Long id,
        String title,
        String description,
        String imageUrl,
        CourseLevel level,
        Integer durationHours,
        String provider,
        CourseFormat format,
        String language,
        Double averageRating,
        Integer ratingCount,
        Boolean active,
        Long categoryId,
        String categoryName
) {}