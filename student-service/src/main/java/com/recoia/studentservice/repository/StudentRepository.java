package com.recoia.studentservice.repository;

import com.recoia.studentservice.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentProfile, Long> {

    // SELECT * FROM student_profiles WHERE user_id = ?
    Optional<StudentProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
