package com.recoia.catalogservice.mapper;

import com.recoia.catalogservice.dto.CourseRequest;
import com.recoia.catalogservice.dto.CourseResponse;
import com.recoia.catalogservice.model.Category;
import com.recoia.catalogservice.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseResponse toResponse(Course c) {
        return new CourseResponse(
                c.getId(), c.getTitle(), c.getDescription(), c.getImageUrl(),
                c.getLevel(), c.getDurationHours(), c.getProvider(), c.getFormat(),
                c.getLanguage(), c.getAverageRating(), c.getRatingCount(), c.getActive(),
                c.getCategory() != null ? c.getCategory().getId() : null,
                c.getCategory() != null ? c.getCategory().getName() : null
        );
    }

    public Course toEntity(CourseRequest r, Category category) {
        Course course = new Course();
        updateEntity(course, r, category);
        return course;
    }

    public void updateEntity(Course course, CourseRequest r, Category category) {
        course.setTitle(r.title());
        course.setDescription(r.description());
        course.setImageUrl(r.imageUrl());
        course.setLevel(r.level());
        course.setDurationHours(r.durationHours());
        course.setProvider(r.provider());
        course.setFormat(r.format());
        course.setLanguage(r.language());
        course.setCategory(category);
    }
}