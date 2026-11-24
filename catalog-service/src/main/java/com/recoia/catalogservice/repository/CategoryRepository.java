package com.recoia.catalogservice.repository;

import com.recoia.catalogservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}