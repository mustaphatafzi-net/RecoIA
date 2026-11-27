package com.recoia.interactionservice.controller;

import com.recoia.interactionservice.dto.CourseCountResponse;
import com.recoia.interactionservice.dto.CourseStatsResponse;
import com.recoia.interactionservice.dto.CourseViewResponse;
import com.recoia.interactionservice.dto.FavoriteResponse;
import com.recoia.interactionservice.dto.RatingRequest;
import com.recoia.interactionservice.dto.RatingResponse;
import com.recoia.interactionservice.exception.ForbiddenException;
import com.recoia.interactionservice.service.InteractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Le userId vient de l'en-tete X-User-Id (ecrit par la gateway a partir du token),
 * jamais de l'adresse : un etudiant ne peut agir que pour lui-meme.
 */
@RestController
@RequestMapping("/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionService interactionService;

    // ---------- notes ----------

    @PutMapping("/ratings/{courseId}")
    public RatingResponse rateCourse(@RequestHeader("X-User-Id") Long userId,
                                     @PathVariable Long courseId,
                                     @Valid @RequestBody RatingRequest request) {
        return interactionService.rateCourse(userId, courseId, request);
    }

    @GetMapping("/ratings/me")
    public List<RatingResponse> getMyRatings(@RequestHeader("X-User-Id") Long userId) {
        return interactionService.getMyRatings(userId);
    }

    // ---------- favoris ----------

    @PostMapping("/favorites/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteResponse addFavorite(@RequestHeader("X-User-Id") Long userId,
                                        @PathVariable Long courseId) {
        return interactionService.addFavorite(userId, courseId);
    }

    @DeleteMapping("/favorites/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavorite(@RequestHeader("X-User-Id") Long userId,
                               @PathVariable Long courseId) {
        interactionService.removeFavorite(userId, courseId);
    }

    @GetMapping("/favorites/me")
    public List<FavoriteResponse> getMyFavorites(@RequestHeader("X-User-Id") Long userId) {
        return interactionService.getMyFavorites(userId);
    }

    // ---------- consultations ----------

    @PostMapping("/views/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseViewResponse recordView(@RequestHeader("X-User-Id") Long userId,
                                         @PathVariable Long courseId) {
        return interactionService.recordView(userId, courseId);
    }

    @GetMapping("/views/me")
    public List<CourseViewResponse> getMyViews(@RequestHeader("X-User-Id") Long userId) {
        return interactionService.getMyViews(userId);
    }

    // ---------- statistiques ----------

    @GetMapping("/courses/{courseId}/stats")
    public CourseStatsResponse getCourseStats(@PathVariable Long courseId) {
        return interactionService.getCourseStats(courseId);
    }

    @GetMapping("/stats/top-viewed")
    public List<CourseCountResponse> getTopViewed(@RequestHeader("X-User-Role") String role,
                                                  @RequestParam(defaultValue = "5") int limit) {
        requireAdmin(role);
        return interactionService.getTopViewed(limit);
    }

    @GetMapping("/stats/top-favorited")
    public List<CourseCountResponse> getTopFavorited(@RequestHeader("X-User-Role") String role,
                                                     @RequestParam(defaultValue = "5") int limit) {
        requireAdmin(role);
        return interactionService.getTopFavorited(limit);
    }

    private void requireAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            throw new ForbiddenException("Acces reserve aux administrateurs");
        }
    }
}
