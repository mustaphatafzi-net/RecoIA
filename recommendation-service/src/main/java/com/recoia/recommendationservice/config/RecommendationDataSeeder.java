package com.recoia.recommendationservice.config;

import com.recoia.recommendationservice.model.Recommendation;
import com.recoia.recommendationservice.model.enums.RecommendationStatus;
import com.recoia.recommendationservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Donnees de test pour le tableau de bord : 3 a 6 recommandations par etudiant (userId 2 a 31)
 * avec un melange d'en attente, acceptees et refusees. Aucun appel a un autre service ici.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationDataSeeder implements CommandLineRunner {

    private static final String[] REFUSAL_REASONS = {
            "Trop difficile", "Pas assez intéressant", "Déjà suivi ailleurs", "Trop long", null
    };

    private final RecommendationRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        Random random = new Random(42);
        List<Recommendation> all = new ArrayList<>();

        for (long userId = 2; userId <= 31; userId++) {
            List<Long> courseIds = new ArrayList<>();
            for (long id = 1; id <= 40; id++) {
                courseIds.add(id);
            }
            Collections.shuffle(courseIds, random);

            int count = 3 + random.nextInt(4);
            for (int i = 0; i < count; i++) {
                Recommendation rec = new Recommendation();
                rec.setUserId(userId);
                rec.setCourseId(courseIds.get(i));
                rec.setScore(40 + random.nextInt(55) + 0.0);
                rec.setReason("Recommandation de test");

                int draw = random.nextInt(100);
                if (draw < 40) {
                    rec.setStatus(RecommendationStatus.PENDING);
                } else if (draw < 75) {
                    rec.setStatus(RecommendationStatus.ACCEPTED);
                    rec.setFeedbackAt(LocalDateTime.now());
                } else {
                    rec.setStatus(RecommendationStatus.REJECTED);
                    rec.setFeedbackReason(REFUSAL_REASONS[random.nextInt(REFUSAL_REASONS.length)]);
                    rec.setFeedbackAt(LocalDateTime.now());
                }
                all.add(rec);
            }
        }
        repository.saveAll(all);
        log.info("{} recommandations de test creees", all.size());
    }
}
