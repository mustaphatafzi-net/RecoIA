package com.recoia.catalogservice.dto;

import com.recoia.catalogservice.model.enums.CourseFormat;
import com.recoia.catalogservice.model.enums.CourseLevel;

public record CourseRequest(
        String title,
        String description,
        String imageUrl,
        CourseLevel level,
        Integer durationHours,
        String provider,
        CourseFormat format,
        String language,
        Long categoryId   // seulement l'id, pas l'objet Category
) {}