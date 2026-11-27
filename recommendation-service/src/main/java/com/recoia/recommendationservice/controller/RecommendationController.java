package com.recoia.recommendationservice.controller;

import com.recoia.recommendationservice.dto.RecommendationResponse;
import com.recoia.recommendationservice.dto.RecommendationStatsResponse;
import com.recoia.recommendationservice.dto.RejectRequest;
import com.recoia.recommendationservice.exception.ForbiddenException;
import com.recoia.recommendationservice.service.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Le userId vient de l'en-tete X-User-Id ecrit par la gateway (jamais de l'adresse). */
@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    // ma liste personnalisee (recalculee a chaque appel)
    @GetMapping("/me")
    public List<RecommendationResponse> getMyRecommendations(@RequestHeader("X-User-Id") Long userId,
                                                             @RequestParam(defaultValue = "10") int limit) {
        return recommendationService.getMyRecommendations(userId, limit);
    }

    // celles auxquelles j'ai deja repondu
    @GetMapping("/me/history")
    public List<RecommendationResponse> getMyHistory(@RequestHeader("X-User-Id") Long userId) {
        return recommendationService.getMyHistory(userId);
    }

    @PostMapping("/{id}/accept")
    public RecommendationResponse accept(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id) {
        return recommendationService.accept(userId, id);
    }

    @PostMapping("/{id}/reject")
    public RecommendationResponse reject(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id,
                                         @Valid @RequestBody(required = false) RejectRequest request) {
        return recommendationService.reject(userId, id, request == null ? null : request.reason());
    }

    // tableau de bord : administrateur seulement
    @GetMapping("/stats")
    public RecommendationStatsResponse getStats(@RequestHeader("X-User-Role") String role,
                                                @RequestParam(defaultValue = "5") int limit) {
        if (!"ADMIN".equals(role)) {
            throw new ForbiddenException("Acces reserve aux administrateurs");
        }
        return recommendationService.getStats(limit);
    }
}
