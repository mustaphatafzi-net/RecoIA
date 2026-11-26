package com.recoia.studentservice.dto;

import com.recoia.studentservice.model.enums.AcademicLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

// Ce que l'etudiant envoie pour creer ou modifier son profil
public record StudentRequest(
        @NotNull(message = "Le niveau d'etudes est obligatoire")
        AcademicLevel academicLevel,

        String professionalGoal,

        // ids de categories (catalog-service)
        Set<Long> categoryIds,

        List<@Valid StudentSkillDto> skills
) {
}
