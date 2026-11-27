package com.recoia.recommendationservice.dto;

import jakarta.validation.constraints.Size;

/** Corps facultatif quand l'etudiant refuse : pourquoi ? */
public record RejectRequest(
        @Size(max = 500, message = "La raison ne doit pas depasser 500 caracteres")
        String reason
) {
}
