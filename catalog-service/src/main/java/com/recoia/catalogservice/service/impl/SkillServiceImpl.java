package com.recoia.catalogservice.service.impl;

import com.recoia.catalogservice.dto.SkillRequest;
import com.recoia.catalogservice.dto.SkillResponse;
import com.recoia.catalogservice.exception.ResourceNotFoundException;
import com.recoia.catalogservice.mapper.SkillMapper;
import com.recoia.catalogservice.model.Skill;
import com.recoia.catalogservice.repository.SkillRepository;
import com.recoia.catalogservice.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    @Override
    public List<SkillResponse> findAll() {
        return skillRepository.findAll().stream().map(skillMapper::toResponse).toList();
    }

    @Override
    public SkillResponse findById(Long id) {
        return skillMapper.toResponse(getSkill(id));
    }

    @Override
    public SkillResponse create(SkillRequest request) {
        return skillMapper.toResponse(skillRepository.save(skillMapper.toEntity(request)));
    }

    @Override
    public SkillResponse update(Long id, SkillRequest request) {
        Skill skill = getSkill(id);
        skillMapper.updateEntity(skill, request);
        return skillMapper.toResponse(skillRepository.save(skill));
    }

    @Override
    public void delete(Long id) {
        skillRepository.delete(getSkill(id));
    }

    private Skill getSkill(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compétence introuvable : " + id));
    }
}
