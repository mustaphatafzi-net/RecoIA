package com.recoia.catalogservice.repository;

import com.recoia.catalogservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Spring écrit la requête SQL à partir du nom de la méthode
    List<Course> findByCategoryId(Long categoryId);

    List<Course> findByTitleContainingIgnoreCase(String keyword);
}