package com.recoia.recommendationservice.dto;

import java.util.List;

/** Chiffres du tableau de bord administrateur. */
public record RecommendationStatsResponse(
        long total,
        long pending,
        long accepted,
        long rejected,
        double acceptanceRate,            // % d'acceptees parmi celles qui ont recu un retour
        List<TopCourseResponse> mostRecommended
) {
}
