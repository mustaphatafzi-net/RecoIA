package com.recoia.interactionservice.dto;

import java.time.LocalDateTime;

public record RatingResponse(Long id, Long userId, Long courseId, Integer score,
                             String comment, LocalDateTime updatedAt) {
}
