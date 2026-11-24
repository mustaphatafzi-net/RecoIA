package com.recoia.catalogservice.service.impl;

import com.recoia.catalogservice.dto.CourseSkillRequest;
import com.recoia.catalogservice.dto.CourseSkillResponse;
import com.recoia.catalogservice.exception.ResourceNotFoundException;
import com.recoia.catalogservice.mapper.CourseSkillMapper;
import com.recoia.catalogservice.model.Course;
import com.recoia.catalogservice.model.CourseSkill;
import com.recoia.catalogservice.model.Skill;
import com.recoia.catalogservice.repository.CourseRepository;
import com.recoia.catalogservice.repository.CourseSkillRepository;
import com.recoia.catalogservice.repository.SkillRepository;
import com.recoia.catalogservice.service.CourseSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSkillServiceImpl implements CourseSkillService {

    private final CourseSkillRepository courseSkillRepository;
    private final CourseRepository courseRepository;
    private final SkillRepository skillRepository;
    private final CourseSkillMapper courseSkillMapper;

    @Override
    public List<CourseSkillResponse> findByCourse(Long courseId) {
        getCourse(courseId); // vérifie que le cours existe (sinon 404)
        return courseSkillRepository.findByCourseId(courseId)
                .stream().map(courseSkillMapper::toResponse).toList();
    }

    @Override
    public CourseSkillResponse add(Long courseId, CourseSkillRequest request) {
        Course course = getCourse(courseId);
        Skill skill = skillRepository.findById(request.skillId())
                .orElseThrow(() -> new ResourceNotFoundException("Compétence introuvable : " + request.skillId()));

        // évite d'ajouter deux fois la même compétence avec le même type
        if (courseSkillRepository.existsByCourseIdAndSkillIdAndType(courseId, skill.getId(), request.type())) {
            throw new IllegalArgumentException("Cette compétence est déjà liée à ce cours avec ce type");
        }

        CourseSkill saved = courseSkillRepository.save(
                courseSkillMapper.toEntity(course, skill, request.type()));
        return courseSkillMapper.toResponse(saved);
    }

    @Override
    public void remove(Long courseId, Long courseSkillId) {
        CourseSkill cs = courseSkillRepository.findById(courseSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("Lien introuvable : " + courseSkillId));
        // le lien doit appartenir au cours indiqué dans l'URL
        if (!cs.getCourse().getId().equals(courseId)) {
            throw new ResourceNotFoundException("Ce lien n'appartient pas au cours " + courseId);
        }
        courseSkillRepository.delete(cs);
    }

    private Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable : " + id));
    }
}
