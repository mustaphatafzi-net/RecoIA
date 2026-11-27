package com.recoia.interactionservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Une consultation : un etudiant a ouvert la fiche d'un cours (plusieurs fois possible). */
@Entity
@Table(name = "course_views")
@Getter @Setter @NoArgsConstructor
public class CourseView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    private LocalDateTime viewedAt;

    @PrePersist
    void onCreate() {
        viewedAt = LocalDateTime.now();
    }
}
