package com.recoia.studentservice.dto;

import com.recoia.studentservice.model.enums.SkillLevel;
import jakarta.validation.constraints.NotNull;

// Une competence : utilisee dans la requete ET dans la reponse
public record StudentSkillDto(
        @NotNull(message = "L'identifiant de la competence est obligatoire")
        Long skillId,

        @NotNull(message = "Le niveau de la competence est obligatoire")
        SkillLevel level
) {
}
