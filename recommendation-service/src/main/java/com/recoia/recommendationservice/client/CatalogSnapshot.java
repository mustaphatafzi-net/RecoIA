package com.recoia.recommendationservice.client;

import com.recoia.recommendationservice.client.dto.CourseData;
import com.recoia.recommendationservice.client.dto.CourseSkillData;

import java.util.List;
import java.util.Map;

/** Photo du catalogue : les cours par id, et les competences de chaque cours. */
public record CatalogSnapshot(Map<Long, CourseData> courses, Map<Long, List<CourseSkillData>> skillsByCourse) {
}
