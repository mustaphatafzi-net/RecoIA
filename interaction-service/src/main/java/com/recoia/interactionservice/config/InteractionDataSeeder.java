package com.recoia.interactionservice.config;

import com.recoia.interactionservice.dto.RatingRequest;
import com.recoia.interactionservice.service.InteractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Donnees de test : pour les 30 etudiants (userId 2 a 31) et les 40 cours du catalogue
 * (courseId 1 a 40) : des notes, des favoris et des consultations aleatoires.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InteractionDataSeeder implements CommandLineRunner {

    private static final int FIRST_USER_ID = 2;
    private static final int LAST_USER_ID = 31;
    private static final int COURSE_COUNT = 40;

    private static final String[] COMMENTS = {
            "Très clair et bien expliqué", "Un peu difficile au début", "Contenu complet",
            "Je recommande", "Trop rapide à mon goût", "Parfait pour débuter", null, null
    };

    private final InteractionService interactionService;

    @Override
    public void run(String... args) {
        if (!interactionService.getMyRatings((long) FIRST_USER_ID).isEmpty()) {
            return; // deja rempli
        }

        Random random = new Random(42);
        int ratings = 0, favorites = 0, views = 0;

        for (long userId = FIRST_USER_ID; userId <= LAST_USER_ID; userId++) {
            // 3 a 8 cours notes (tous differents)
            List<Long> courseIds = shuffledCourseIds(random);
            int ratingCount = 3 + random.nextInt(6);
            for (int i = 0; i < ratingCount; i++) {
                int score = 1 + random.nextInt(5);
                String comment = COMMENTS[random.nextInt(COMMENTS.length)];
                interactionService.rateCourse(userId, courseIds.get(i), new RatingRequest(score, comment));
                ratings++;
            }

            // 1 a 4 favoris
            courseIds = shuffledCourseIds(random);
            int favoriteCount = 1 + random.nextInt(4);
            for (int i = 0; i < favoriteCount; i++) {
                interactionService.addFavorite(userId, courseIds.get(i));
                favorites++;
            }

            // 5 a 15 consultations (un meme cours peut etre vu plusieurs fois)
            int viewCount = 5 + random.nextInt(11);
            for (int i = 0; i < viewCount; i++) {
                interactionService.recordView(userId, 1L + random.nextInt(COURSE_COUNT));
                views++;
            }
        }

        log.info("Interactions de test creees : {} notes, {} favoris, {} consultations",
                ratings, favorites, views);
    }

    private List<Long> shuffledCourseIds(Random random) {
        List<Long> ids = new ArrayList<>();
        for (long id = 1; id <= COURSE_COUNT; id++) {
            ids.add(id);
        }
        Collections.shuffle(ids, random);
        return ids;
    }
}
