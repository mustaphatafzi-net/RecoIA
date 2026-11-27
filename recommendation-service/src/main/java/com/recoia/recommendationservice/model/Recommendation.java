package com.recoia.recommendationservice.model;

import com.recoia.recommendationservice.model.enums.RecommendationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Un cours recommande a un etudiant, avec son score, sa raison et le retour de l'etudiant. */
@Entity
@Table(name = "recommendations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
@Getter @Setter @NoArgsConstructor
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // numero du compte (auth-service) : simple nombre, pas de lien entre bases
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // numero du cours (catalog-service)
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    // note de 0 a 100 calculee par le moteur
    @Column(nullable = false)
    private Double score;

    // phrase qui explique pourquoi ce cours est propose
    @Column(length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationStatus status = RecommendationStatus.PENDING;

    // pourquoi l'etudiant a refuse (facultatif)
    @Column(length = 500)
    private String feedbackReason;

    private LocalDateTime createdAt;
    private LocalDateTime feedbackAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
