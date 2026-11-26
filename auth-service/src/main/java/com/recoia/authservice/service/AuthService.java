package com.recoia.authservice.service;

import com.recoia.authservice.dto.AuthResponse;
import com.recoia.authservice.dto.LoginRequest;
import com.recoia.authservice.dto.RegisterRequest;
import com.recoia.authservice.dto.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    UserResponse createAdmin(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
