package com.recoia.authservice.controller;

import com.recoia.authservice.dto.AuthResponse;
import com.recoia.authservice.dto.LoginRequest;
import com.recoia.authservice.dto.RegisterRequest;
import com.recoia.authservice.dto.UserResponse;
import com.recoia.authservice.exception.ForbiddenException;
import com.recoia.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse created = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // X-User-Role est ecrit par la gateway apres verification du token
    @PostMapping("/admins")
    public ResponseEntity<UserResponse> createAdmin(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody RegisterRequest request) {
        if (!"ADMIN".equals(role)) {
            throw new ForbiddenException();
        }
        UserResponse created = authService.createAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
