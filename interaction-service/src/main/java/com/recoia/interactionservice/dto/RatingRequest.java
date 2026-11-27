package com.recoia.interactionservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Ce que l'etudiant envoie pour noter un cours (le numero du cours est dans l'adresse). */
public record RatingRequest(
        @NotNull(message = "La note est obligatoire")
        @Min(value = 1, message = "La note doit etre comprise entre 1 et 5")
        @Max(value = 5, message = "La note doit etre comprise entre 1 et 5")
        Integer score,

        @Size(max = 1000, message = "Le commentaire ne doit pas depasser 1000 caracteres")
        String comment
) {
}
