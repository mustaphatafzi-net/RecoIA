package com.recoia.catalogservice.service;

import com.recoia.catalogservice.dto.CourseRequest;
import com.recoia.catalogservice.dto.CourseResponse;

import java.util.List;

public interface CourseService {

    List<CourseResponse> findAll(String keyword);

    CourseResponse findById(Long id);

    CourseResponse create(CourseRequest request);

    CourseResponse update(Long id, CourseRequest request);

    void delete(Long id);
}