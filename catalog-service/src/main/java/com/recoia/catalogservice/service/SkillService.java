package com.recoia.catalogservice.service;

import com.recoia.catalogservice.dto.SkillRequest;
import com.recoia.catalogservice.dto.SkillResponse;

import java.util.List;

public interface SkillService {
    List<SkillResponse> findAll();
    SkillResponse findById(Long id);
    SkillResponse create(SkillRequest request);
    SkillResponse update(Long id, SkillRequest request);
    void delete(Long id);
}
