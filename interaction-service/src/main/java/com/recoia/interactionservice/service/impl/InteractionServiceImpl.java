package com.recoia.interactionservice.service.impl;

import com.recoia.interactionservice.dto.CourseCountResponse;
import com.recoia.interactionservice.dto.CourseStatsResponse;
import com.recoia.interactionservice.dto.CourseViewResponse;
import com.recoia.interactionservice.dto.FavoriteResponse;
import com.recoia.interactionservice.dto.RatingRequest;
import com.recoia.interactionservice.dto.RatingResponse;
import com.recoia.interactionservice.mapper.InteractionMapper;
import com.recoia.interactionservice.model.CourseView;
import com.recoia.interactionservice.model.Favorite;
import com.recoia.interactionservice.model.Rating;
import com.recoia.interactionservice.repository.CourseViewRepository;
import com.recoia.interactionservice.repository.FavoriteRepository;
import com.recoia.interactionservice.repository.RatingRepository;
import com.recoia.interactionservice.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {

    private final RatingRepository ratingRepository;
    private final FavoriteRepository favoriteRepository;
    private final CourseViewRepository courseViewRepository;
    private final InteractionMapper mapper;

    // ---------- notes ----------

    @Override
    @Transactional
    public RatingResponse rateCourse(Long userId, Long courseId, RatingRequest request) {
        // a-t-il deja note ce cours ? oui -> on corrige sa note, non -> on en cree une
        Rating rating = ratingRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseGet(() -> {
                    Rating created = new Rating();
                    created.setUserId(userId);
                    created.setCourseId(courseId);
                    return created;
                });
        rating.setScore(request.score());
        rating.setComment(request.comment());
        return mapper.toResponse(ratingRepository.save(rating));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponse> getMyRatings(Long userId) {
        return ratingRepository.findByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    // ---------- favoris ----------

    @Override
    @Transactional
    public FavoriteResponse addFavorite(Long userId, Long courseId) {
        // si le cours est deja en favori, on renvoie l'existant (pas de doublon)
        Favorite favorite = favoriteRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseGet(() -> {
                    Favorite created = new Favorite();
                    created.setUserId(userId);
                    created.setCourseId(courseId);
                    return favoriteRepository.save(created);
                });
        return mapper.toResponse(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long courseId) {
        favoriteRepository.deleteByUserIdAndCourseId(userId, courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteResponse> getMyFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    // ---------- consultations ----------

    @Override
    @Transactional
    public CourseViewResponse recordView(Long userId, Long courseId) {
        CourseView view = new CourseView();
        view.setUserId(userId);
        view.setCourseId(courseId);
        return mapper.toResponse(courseViewRepository.save(view));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseViewResponse> getMyViews(Long userId) {
        return courseViewRepository.findByUserIdOrderByViewedAtDesc(userId)
                .stream().map(mapper::toResponse).toList();
    }

    // ---------- statistiques ----------

    @Override
    @Transactional(readOnly = true)
    public CourseStatsResponse getCourseStats(Long courseId) {
        Double average = ratingRepository.averageScoreByCourseId(courseId);
        double rounded = average == null ? 0.0 : Math.round(average * 100.0) / 100.0;
        return new CourseStatsResponse(courseId, rounded,
                ratingRepository.countByCourseId(courseId),
                favoriteRepository.countByCourseId(courseId),
                courseViewRepository.countByCourseId(courseId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseCountResponse> getTopViewed(int limit) {
        return courseViewRepository.findTopViewed(PageRequest.of(0, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseCountResponse> getTopFavorited(int limit) {
        return favoriteRepository.findTopFavorited(PageRequest.of(0, limit));
    }
}
