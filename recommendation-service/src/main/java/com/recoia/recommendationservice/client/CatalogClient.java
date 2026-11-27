package com.recoia.recommendationservice.client;

import com.recoia.recommendationservice.client.dto.CourseData;
import com.recoia.recommendationservice.client.dto.CourseSkillData;
import com.recoia.recommendationservice.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Va chercher le catalogue chez catalog-service. Comme il faut 1 appel par cours pour les competences,
 * on garde le resultat 60 secondes en memoire (cache) au lieu de tout redemander a chaque requete.
 */
@Component
@RequiredArgsConstructor
public class CatalogClient {

    private static final long CACHE_MILLIS = 60_000;

    private final ServiceUrlResolver resolver;
    private final RestClient restClient = RestClient.create();

    private CatalogSnapshot cache;
    private long cachedAt;

    public synchronized CatalogSnapshot snapshot() {
        if (cache == null || System.currentTimeMillis() - cachedAt > CACHE_MILLIS) {
            cache = load();
            cachedAt = System.currentTimeMillis();
        }
        return cache;
    }

    private CatalogSnapshot load() {
        try {
            String base = resolver.url("catalog-service");

            List<CourseData> courses = restClient.get()
                    .uri(base + "/courses")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CourseData>>() {});

            Map<Long, CourseData> coursesById = new HashMap<>();
            Map<Long, List<CourseSkillData>> skillsByCourse = new HashMap<>();
            if (courses != null) {
                for (CourseData course : courses) {
                    coursesById.put(course.id(), course);
                    List<CourseSkillData> skills = restClient.get()
                            .uri(base + "/courses/" + course.id() + "/skills")
                            .retrieve()
                            .body(new ParameterizedTypeReference<List<CourseSkillData>>() {});
                    skillsByCourse.put(course.id(), skills == null ? List.of() : skills);
                }
            }
            return new CatalogSnapshot(coursesById, skillsByCourse);
        } catch (RestClientException e) {
            throw new ServiceUnavailableException("catalog-service indisponible");
        }
    }
}
