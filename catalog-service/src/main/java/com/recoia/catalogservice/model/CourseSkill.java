package com.recoia.catalogservice.model;

import com.recoia.catalogservice.model.enums.CourseSkillType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class CourseSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private CourseSkillType type;
}