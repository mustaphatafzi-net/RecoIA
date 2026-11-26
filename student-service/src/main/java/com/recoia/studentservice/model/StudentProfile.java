package com.recoia.studentservice.model;

import com.recoia.studentservice.model.enums.AcademicLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // simple nombre : l'id du compte dans auth-service, recu via l'en-tete X-User-Id
    // (aucune verification automatique : les deux services ont chacun leur base)
    @Column(nullable = false, unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicLevel academicLevel;

    private String professionalGoal;

    // 1 profil -> 0..* interets. La colonne student_profile_id est creee dans student_interests.
    // cascade ALL : sauver le profil sauve ses interets ; orphanRemoval : retirer un interet de la liste le supprime
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "student_profile_id")
    private Set<StudentInterest> interests = new HashSet<>();

    // 1 profil -> 0..* competences
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "student_profile_id")
    private Set<StudentSkill> skills = new HashSet<>();
}
