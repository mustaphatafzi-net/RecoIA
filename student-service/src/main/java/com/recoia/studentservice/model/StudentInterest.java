package com.recoia.studentservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Un centre d'interet = une categorie du catalog-service
@Entity
@Table(name = "student_interests")
@Getter
@Setter
@NoArgsConstructor
public class StudentInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // simple nombre : l'id de la Category dans catalog-service (aucune verification automatique)
    @Column(nullable = false)
    private Long categoryId;

    public StudentInterest(Long categoryId) {
        this.categoryId = categoryId;
    }
}
