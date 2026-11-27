package com.recoia.interactionservice.service;

import com.recoia.interactionservice.dto.CourseCountResponse;
import com.recoia.interactionservice.dto.CourseStatsResponse;
import com.recoia.interactionservice.dto.CourseViewResponse;
import com.recoia.interactionservice.dto.FavoriteResponse;
import com.recoia.interactionservice.dto.RatingRequest;
import com.recoia.interactionservice.dto.RatingResponse;

import java.util.List;

public interface InteractionService {

    // notes : cree la note ou la modifie si l'etudiant avait deja note ce cours
    RatingResponse rateCourse(Long userId, Long courseId, RatingRequest request);
    List<RatingResponse> getMyRatings(Long userId);

    // favoris
    FavoriteResponse addFavorite(Long userId, Long courseId);
    void removeFavorite(Long userId, Long courseId);
    List<FavoriteResponse> getMyFavorites(Long userId);

    // consultations
    CourseViewResponse recordView(Long userId, Long courseId);
    List<CourseViewResponse> getMyViews(Long userId);

    // statistiques
    CourseStatsResponse getCourseStats(Long courseId);
    List<CourseCountResponse> getTopViewed(int limit);
    List<CourseCountResponse> getTopFavorited(int limit);
}
