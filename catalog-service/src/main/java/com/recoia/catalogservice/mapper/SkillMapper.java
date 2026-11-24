package com.recoia.catalogservice.mapper;

import com.recoia.catalogservice.dto.SkillRequest;
import com.recoia.catalogservice.dto.SkillResponse;
import com.recoia.catalogservice.model.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public SkillResponse toResponse(Skill s) {
        return new SkillResponse(s.getId(), s.getName(), s.getDescription());
    }

    public Skill toEntity(SkillRequest r) {
        Skill skill = new Skill();
        updateEntity(skill, r);
        return skill;
    }

    public void updateEntity(Skill skill, SkillRequest r) {
        skill.setName(r.name());
        skill.setDescription(r.description());
    }
}
