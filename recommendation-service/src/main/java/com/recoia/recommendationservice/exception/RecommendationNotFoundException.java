package com.recoia.recommendationservice.exception;

public class RecommendationNotFoundException extends RuntimeException {

    public RecommendationNotFoundException(Long id) {
        super("Recommandation introuvable : " + id);
    }
}
