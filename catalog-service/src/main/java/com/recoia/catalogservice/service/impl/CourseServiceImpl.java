package com.recoia.catalogservice.service.impl;

import com.recoia.catalogservice.dto.CourseRequest;
import com.recoia.catalogservice.dto.CourseResponse;
import com.recoia.catalogservice.exception.ResourceNotFoundException;
import com.recoia.catalogservice.mapper.CourseMapper;
import com.recoia.catalogservice.model.Category;
import com.recoia.catalogservice.model.Course;
import com.recoia.catalogservice.repository.CategoryRepository;
import com.recoia.catalogservice.repository.CourseRepository;
import com.recoia.catalogservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service                      // Spring crée l'instance : c'est CE bean qui sera injecté
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseResponse> findAll(String keyword) {
        List<Course> courses = (keyword == null || keyword.isBlank())
                ? courseRepository.findAll()
                : courseRepository.findByTitleContainingIgnoreCase(keyword);
        return courses.stream().map(courseMapper::toResponse).toList();
    }

    @Override
    public CourseResponse findById(Long id) {
        return courseMapper.toResponse(getCourse(id));
    }

    @Override
    public CourseResponse create(CourseRequest request) {
        Category category = getCategory(request.categoryId());
        Course course = courseMapper.toEntity(request, category);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = getCourse(id);
        courseMapper.updateEntity(course, request, getCategory(request.categoryId()));
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public void delete(Long id) {
        courseRepository.delete(getCourse(id));
    }

    private Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable : " + id));
    }

    private Category getCategory(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + categoryId));
    }
}