package com.recoia.catalogservice.mapper;

import com.recoia.catalogservice.dto.CategoryRequest;
import com.recoia.catalogservice.dto.CategoryResponse;
import com.recoia.catalogservice.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getDescription());
    }

    public Category toEntity(CategoryRequest r) {
        Category category = new Category();
        updateEntity(category, r);
        return category;
    }

    public void updateEntity(Category category, CategoryRequest r) {
        category.setName(r.name());
        category.setDescription(r.description());
    }
}