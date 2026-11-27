package com.recoia.recommendationservice.service;

import com.recoia.recommendationservice.dto.RecommendationResponse;
import com.recoia.recommendationservice.dto.RecommendationStatsResponse;

import java.util.List;

public interface RecommendationService {

    // calcule (ou recalcule) la liste personnalisee de l'etudiant
    List<RecommendationResponse> getMyRecommendations(Long userId, int limit);

    // recommandations deja acceptees ou refusees
    List<RecommendationResponse> getMyHistory(Long userId);

    // retours de l'etudiant
    RecommendationResponse accept(Long userId, Long recommendationId);
    RecommendationResponse reject(Long userId, Long recommendationId, String reason);

    // tableau de bord admin
    RecommendationStatsResponse getStats(int limit);
}
