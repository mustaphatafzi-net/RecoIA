package com.recoia.catalogservice.service;

import com.recoia.catalogservice.dto.CategoryRequest;
import com.recoia.catalogservice.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> findAll();
    CategoryResponse findById(Long id);
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Long id, CategoryRequest request);
    void delete(Long id);
}