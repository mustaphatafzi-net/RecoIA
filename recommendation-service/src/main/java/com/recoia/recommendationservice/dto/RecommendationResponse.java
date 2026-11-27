package com.recoia.recommendationservice.dto;

import com.recoia.recommendationservice.model.enums.RecommendationStatus;

import java.time.LocalDateTime;

/** Ce que l'etudiant voit : la recommandation + les infos du cours pour l'afficher. */
public record RecommendationResponse(
        Long id,
        Long courseId,
        String courseTitle,
        String categoryName,
        String imageUrl,
        String level,
        Double score,
        String reason,
        RecommendationStatus status,
        String feedbackReason,
        LocalDateTime createdAt
) {
}
