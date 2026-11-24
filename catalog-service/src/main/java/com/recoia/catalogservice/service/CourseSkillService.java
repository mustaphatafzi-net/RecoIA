package com.recoia.catalogservice.service;

import com.recoia.catalogservice.dto.CourseSkillRequest;
import com.recoia.catalogservice.dto.CourseSkillResponse;

import java.util.List;

public interface CourseSkillService {

    List<CourseSkillResponse> findByCourse(Long courseId);

    CourseSkillResponse add(Long courseId, CourseSkillRequest request);

    void remove(Long courseId, Long courseSkillId);
}
