package com.recoia.recommendationservice.engine;

import com.recoia.recommendationservice.client.dto.CourseData;

/** Un cours avec son score (0 a 100) et la phrase qui explique pourquoi. */
public record ScoredCourse(CourseData course, double score, String reason) {
}
