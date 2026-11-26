package com.recoia.studentservice.service;

import com.recoia.studentservice.dto.StudentRequest;
import com.recoia.studentservice.dto.StudentResponse;

import java.util.List;

public interface StudentService {

    // Mon profil (404 s'il n'existe pas encore)
    StudentResponse getMyProfile(Long userId);

    // Cree mon profil s'il n'existe pas, sinon le modifie
    StudentResponse saveMyProfile(Long userId, StudentRequest request);

    // Tous les etudiants (reserve a l'admin)
    List<StudentResponse> getAll();
}
