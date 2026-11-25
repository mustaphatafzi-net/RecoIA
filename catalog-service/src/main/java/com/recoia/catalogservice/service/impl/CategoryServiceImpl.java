package com.recoia.catalogservice.service.impl;

import com.recoia.catalogservice.dto.CategoryRequest;
import com.recoia.catalogservice.dto.CategoryResponse;
import com.recoia.catalogservice.exception.ResourceNotFoundException;
import com.recoia.catalogservice.mapper.CategoryMapper;
import com.recoia.catalogservice.model.Category;
import com.recoia.catalogservice.repository.CategoryRepository;
import com.recoia.catalogservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).toList();
    }

    @Override
    public CategoryResponse findById(Long id) {

        return categoryMapper.toResponse(getCategory(id));
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getCategory(id);
        categoryMapper.updateEntity(category, request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        categoryRepository.delete(getCategory(id));
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + id));
    }
}