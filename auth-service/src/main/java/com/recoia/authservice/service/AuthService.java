package com.recoia.authservice.service;

import com.recoia.authservice.dto.RegisterRequest;
import com.recoia.authservice.dto.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);
}
