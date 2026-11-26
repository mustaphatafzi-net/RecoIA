package com.recoia.studentservice.controller;

import com.recoia.studentservice.dto.StudentRequest;
import com.recoia.studentservice.dto.StudentResponse;
import com.recoia.studentservice.exception.ForbiddenException;
import com.recoia.studentservice.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // X-User-Id et X-User-Role sont ecrits par la gateway apres verification du token

    @GetMapping("/me")
    public ResponseEntity<StudentResponse> getMyProfile(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(studentService.getMyProfile(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<StudentResponse> saveMyProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.saveMyProfile(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAll(
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (!"ADMIN".equals(role)) {
            throw new ForbiddenException();
        }
        return ResponseEntity.ok(studentService.getAll());
    }
}
