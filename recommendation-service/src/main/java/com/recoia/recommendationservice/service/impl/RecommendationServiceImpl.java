package com.recoia.recommendationservice.service.impl;

import com.recoia.recommendationservice.client.CatalogClient;
import com.recoia.recommendationservice.client.CatalogSnapshot;
import com.recoia.recommendationservice.client.InteractionClient;
import com.recoia.recommendationservice.client.StudentClient;
import com.recoia.recommendationservice.client.dto.FavoriteData;
import com.recoia.recommendationservice.client.dto.RatingData;
import com.recoia.recommendationservice.client.dto.StudentData;
import com.recoia.recommendationservice.client.dto.ViewData;
import com.recoia.recommendationservice.dto.CourseCountResponse;
import com.recoia.recommendationservice.dto.RecommendationResponse;
import com.recoia.recommendationservice.dto.RecommendationStatsResponse;
import com.recoia.recommendationservice.dto.TopCourseResponse;
import com.recoia.recommendationservice.engine.ScoredCourse;
import com.recoia.recommendationservice.engine.ScoringEngine;
import com.recoia.recommendationservice.engine.ScoringInput;
import com.recoia.recommendationservice.exception.RecommendationNotFoundException;
import com.recoia.recommendationservice.mapper.RecommendationMapper;
import com.recoia.recommendationservice.model.Recommendation;
import com.recoia.recommendationservice.model.enums.RecommendationStatus;
import com.recoia.recommendationservice.repository.RecommendationRepository;
import com.recoia.recommendationservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository repository;
    private final StudentClient studentClient;
    private final InteractionClient interactionClient;
    private final CatalogClient catalogClient;
    private final ScoringEngine engine;
    private final RecommendationMapper mapper;

    @Override
    @Transactional
    public List<RecommendationResponse> getMyRecommendations(Long userId, int limit) {
        int size = Math.max(1, Math.min(limit, 50));

        // 1) rassembler ce qu'on sait de l'etudiant (profil + activite) et le catalogue
        StudentData profile = studentClient.getProfile(userId).orElse(null);
        List<RatingData> ratings = interactionClient.getRatings(userId);
        List<FavoriteData> favorites = interactionClient.getFavorites(userId);
        List<ViewData> views = interactionClient.getViews(userId);
        CatalogSnapshot catalog = catalogClient.snapshot();

        // 2) ne pas reproposer : les cours deja notes, ni ceux deja acceptes / refuses
        List<Recommendation> history = repository.findByUserIdAndStatusIn(userId,
                List.of(RecommendationStatus.ACCEPTED, RecommendationStatus.REJECTED));
        Set<Long> excluded = new HashSet<>();
        ratings.forEach(r -> excluded.add(r.courseId()));
        history.forEach(r -> excluded.add(r.getCourseId()));

        // 3) noter tous les cours et garder les meilleurs
        ScoringInput input = new ScoringInput(profile, catalog.courses().values(), catalog.skillsByCourse(),
                ratings, favorites, views, history, excluded);
        List<ScoredCourse> top = engine.score(input).stream()
                .sorted(Comparator.comparingDouble(ScoredCourse::score).reversed())
                .limit(size)
                .toList();

        // 4) enregistrer : on met a jour les recommandations en attente, on retire les anciennes
        List<Recommendation> pending = repository.findByUserIdAndStatus(userId, RecommendationStatus.PENDING);
        Map<Long, Recommendation> pendingByCourse = pending.stream()
                .collect(Collectors.toMap(Recommendation::getCourseId, r -> r, (a, b) -> a));
        Set<Long> keptCourseIds = top.stream().map(s -> s.course().id()).collect(Collectors.toSet());
        repository.deleteAll(pending.stream().filter(r -> !keptCourseIds.contains(r.getCourseId())).toList());

        List<RecommendationResponse> responses = new ArrayList<>();
        for (ScoredCourse scored : top) {
            Recommendation rec = pendingByCourse.get(scored.course().id());
            if (rec == null) {
                rec = new Recommendation();
                rec.setUserId(userId);
                rec.setCourseId(scored.course().id());
                rec.setStatus(RecommendationStatus.PENDING);
            }
            rec.setScore(scored.score());
            rec.setReason(scored.reason());
            responses.add(mapper.toResponse(repository.save(rec), scored.course()));
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecommendationResponse> getMyHistory(Long userId) {
        CatalogSnapshot catalog = catalogClient.snapshot();
        return repository.findByUserIdAndStatusIn(userId,
                        List.of(RecommendationStatus.ACCEPTED, RecommendationStatus.REJECTED))
                .stream()
                .sorted(Comparator.comparing(Recommendation::getFeedbackAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(r -> mapper.toResponse(r, catalog.courses().get(r.getCourseId())))
                .toList();
    }

    @Override
    @Transactional
    public RecommendationResponse accept(Long userId, Long recommendationId) {
        return giveFeedback(userId, recommendationId, RecommendationStatus.ACCEPTED, null);
    }

    @Override
    @Transactional
    public RecommendationResponse reject(Long userId, Long recommendationId, String reason) {
        return giveFeedback(userId, recommendationId, RecommendationStatus.REJECTED, reason);
    }

    private RecommendationResponse giveFeedback(Long userId, Long recommendationId,
                                                RecommendationStatus status, String reason) {
        // findByIdAndUserId : si la recommandation appartient a un autre etudiant, on repond "introuvable"
        Recommendation rec = repository.findByIdAndUserId(recommendationId, userId)
                .orElseThrow(() -> new RecommendationNotFoundException(recommendationId));
        rec.setStatus(status);
        rec.setFeedbackReason(reason);
        rec.setFeedbackAt(LocalDateTime.now());
        Recommendation saved = repository.save(rec);
        return mapper.toResponse(saved, catalogClient.snapshot().courses().get(saved.getCourseId()));
    }

    @Override
    @Transactional(readOnly = true)
    public RecommendationStatsResponse getStats(int limit) {
        long pending = repository.countByStatus(RecommendationStatus.PENDING);
        long accepted = repository.countByStatus(RecommendationStatus.ACCEPTED);
        long rejected = repository.countByStatus(RecommendationStatus.REJECTED);
        long answered = accepted + rejected;
        double rate = answered == 0 ? 0 : Math.round(accepted * 1000.0 / answered) / 10.0;

        CatalogSnapshot catalog = catalogClient.snapshot();
        List<TopCourseResponse> top = new ArrayList<>();
        for (CourseCountResponse line : repository.findMostRecommended(PageRequest.of(0, limit))) {
            var course = catalog.courses().get(line.courseId());
            top.add(new TopCourseResponse(line.courseId(), course == null ? null : course.title(), line.total()));
        }
        return new RecommendationStatsResponse(pending + accepted + rejected, pending, accepted, rejected, rate, top);
    }
}
