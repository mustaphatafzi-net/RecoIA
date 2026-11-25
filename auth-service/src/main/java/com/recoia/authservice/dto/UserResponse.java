package com.recoia.authservice.dto;

import com.recoia.authservice.model.enums.Role;

public record UserResponse(
        Long id,
        String email,
        Role role
) {}
