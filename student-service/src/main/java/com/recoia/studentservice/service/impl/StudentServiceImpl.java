package com.recoia.studentservice.service.impl;

import com.recoia.studentservice.dto.StudentRequest;
import com.recoia.studentservice.dto.StudentResponse;
import com.recoia.studentservice.exception.StudentNotFoundException;
import com.recoia.studentservice.mapper.StudentMapper;
import com.recoia.studentservice.model.StudentProfile;
import com.recoia.studentservice.repository.StudentRepository;
import com.recoia.studentservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    // @Transactional est necessaire : les interets et competences sont charges a la demande (LAZY),
    // il faut que la transaction soit ouverte pendant toResponse
    @Override
    @Transactional(readOnly = true)
    public StudentResponse getMyProfile(Long userId) {
        StudentProfile profile = studentRepository.findByUserId(userId)
                .orElseThrow(StudentNotFoundException::new);
        return studentMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public StudentResponse saveMyProfile(Long userId, StudentRequest request) {
        StudentProfile profile = studentRepository.findByUserId(userId).orElse(null);

        if (profile == null) {
            // premier appel : on cree le profil
            profile = studentMapper.toEntity(request, userId);
        } else {
            // profil existant : on le modifie
            studentMapper.updateEntity(profile, request);
        }

        StudentProfile saved = studentRepository.save(profile);
        return studentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponse)
                .toList();
    }
}
