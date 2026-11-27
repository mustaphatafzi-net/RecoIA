package com.recoia.interactionservice.dto;

import java.time.LocalDateTime;

public record FavoriteResponse(Long id, Long userId, Long courseId, LocalDateTime createdAt) {
}
