package com.recoia.studentservice.model;

import com.recoia.studentservice.model.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Une competence de l'etudiant = un Skill du catalog-service + son niveau
@Entity
@Table(name = "student_skills")
@Getter
@Setter
@NoArgsConstructor
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // simple nombre : l'id du Skill dans catalog-service (aucune verification automatique)
    @Column(nullable = false)
    private Long skillId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel level;

    public StudentSkill(Long skillId, SkillLevel level) {
        this.skillId = skillId;
        this.level = level;
    }
}
