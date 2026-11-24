package com.recoia.catalogservice.mapper;

import com.recoia.catalogservice.dto.CourseSkillResponse;
import com.recoia.catalogservice.model.Course;
import com.recoia.catalogservice.model.CourseSkill;
import com.recoia.catalogservice.model.Skill;
import com.recoia.catalogservice.model.enums.CourseSkillType;
import org.springframework.stereotype.Component;

@Component
public class CourseSkillMapper {

    public CourseSkillResponse toResponse(CourseSkill cs) {
        return new CourseSkillResponse(
                cs.getId(),
                cs.getCourse().getId(),
                cs.getSkill().getId(),
                cs.getSkill().getName(),
                cs.getType()
        );
    }

    public CourseSkill toEntity(Course course, Skill skill, CourseSkillType type) {
        CourseSkill cs = new CourseSkill();
        cs.setCourse(course);
        cs.setSkill(skill);
        cs.setType(type);
        return cs;
    }
}
