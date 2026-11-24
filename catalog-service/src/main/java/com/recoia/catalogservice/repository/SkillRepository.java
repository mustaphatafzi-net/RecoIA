package com.recoia.catalogservice.repository;

import com.recoia.catalogservice.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}