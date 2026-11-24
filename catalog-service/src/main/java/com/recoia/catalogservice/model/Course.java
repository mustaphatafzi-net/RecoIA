package com.recoia.catalogservice.model;

import com.recoia.catalogservice.model.enums.CourseFormat;
import com.recoia.catalogservice.model.enums.CourseLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)          // stocke "BEGINNER" et non 0
    private CourseLevel level;

    private Integer durationHours;
    private String provider;

    @Enumerated(EnumType.STRING)
    private CourseFormat format;

    private String language;
    private Double averageRating = 0.0;
    private Integer ratingCount = 0;
    private Boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne                            // plusieurs cours → une catégorie
    private Category category;

    @PrePersist                           // exécuté juste avant l'insertion
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate                            // exécuté juste avant une modification
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}