package com.recoia.recommendationservice.client;

import com.recoia.recommendationservice.client.dto.StudentData;
import com.recoia.recommendationservice.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

/** Va chercher le profil d'un etudiant chez student-service. */
@Component
@RequiredArgsConstructor
public class StudentClient {

    private final ServiceUrlResolver resolver;
    private final RestClient restClient = RestClient.create();

    public Optional<StudentData> getProfile(Long userId) {
        try {
            StudentData profile = restClient.get()
                    .uri(resolver.url("student-service") + "/students/me")
                    .header("X-User-Id", String.valueOf(userId))   // comme le ferait la gateway
                    .retrieve()
                    .body(StudentData.class);
            return Optional.ofNullable(profile);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();   // l'etudiant n'a pas encore rempli son profil
        } catch (RestClientException e) {
            throw new ServiceUnavailableException("student-service indisponible");
        }
    }
}
