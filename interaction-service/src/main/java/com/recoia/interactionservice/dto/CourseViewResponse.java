package com.recoia.interactionservice.dto;

import java.time.LocalDateTime;

public record CourseViewResponse(Long id, Long userId, Long courseId, LocalDateTime viewedAt) {
}
