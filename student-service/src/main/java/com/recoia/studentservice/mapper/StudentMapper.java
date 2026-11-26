package com.recoia.studentservice.mapper;

import com.recoia.studentservice.dto.StudentRequest;
import com.recoia.studentservice.dto.StudentResponse;
import com.recoia.studentservice.dto.StudentSkillDto;
import com.recoia.studentservice.model.StudentInterest;
import com.recoia.studentservice.model.StudentProfile;
import com.recoia.studentservice.model.StudentSkill;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StudentMapper {

    // Requete + userId (recu de la gateway) -> nouveau profil
    public StudentProfile toEntity(StudentRequest request, Long userId) {
        StudentProfile profile = new StudentProfile();
        profile.setUserId(userId);
        updateEntity(profile, request);
        return profile;
    }

    // Copie les champs de la requete dans un profil deja existant
    public void updateEntity(StudentProfile profile, StudentRequest request) {
        profile.setAcademicLevel(request.academicLevel());
        profile.setProfessionalGoal(request.professionalGoal());

        // on vide puis on remplit : les anciens interets/competences sont supprimes (orphanRemoval)
        profile.getInterests().clear();
        if (request.categoryIds() != null) {
            request.categoryIds().forEach(categoryId ->
                    profile.getInterests().add(new StudentInterest(categoryId)));
        }

        profile.getSkills().clear();
        if (request.skills() != null) {
            request.skills().forEach(skill ->
                    profile.getSkills().add(new StudentSkill(skill.skillId(), skill.level())));
        }
    }

    // Profil -> ce qu'on renvoie
    public StudentResponse toResponse(StudentProfile profile) {
        return new StudentResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getAcademicLevel(),
                profile.getProfessionalGoal(),
                profile.getInterests().stream()
                        .map(StudentInterest::getCategoryId)
                        .collect(Collectors.toSet()),
                profile.getSkills().stream()
                        .map(skill -> new StudentSkillDto(skill.getSkillId(), skill.getLevel()))
                        .toList()
        );
    }
}
