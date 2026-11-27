package com.recoia.recommendationservice.client;

import com.recoia.recommendationservice.client.dto.FavoriteData;
import com.recoia.recommendationservice.client.dto.RatingData;
import com.recoia.recommendationservice.client.dto.ViewData;
import com.recoia.recommendationservice.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

/** Va chercher les notes, favoris et consultations d'un etudiant chez interaction-service. */
@Component
@RequiredArgsConstructor
public class InteractionClient {

    private final ServiceUrlResolver resolver;
    private final RestClient restClient = RestClient.create();

    public List<RatingData> getRatings(Long userId) {
        return fetch(userId, "/interactions/ratings/me", new ParameterizedTypeReference<List<RatingData>>() {});
    }

    public List<FavoriteData> getFavorites(Long userId) {
        return fetch(userId, "/interactions/favorites/me", new ParameterizedTypeReference<List<FavoriteData>>() {});
    }

    public List<ViewData> getViews(Long userId) {
        return fetch(userId, "/interactions/views/me", new ParameterizedTypeReference<List<ViewData>>() {});
    }

    private <T> List<T> fetch(Long userId, String path, ParameterizedTypeReference<List<T>> type) {
        try {
            List<T> result = restClient.get()
                    .uri(resolver.url("interaction-service") + path)
                    .header("X-User-Id", String.valueOf(userId))
                    .retrieve()
                    .body(type);
            return result == null ? List.of() : result;
        } catch (RestClientException e) {
            throw new ServiceUnavailableException("interaction-service indisponible");
        }
    }
}
