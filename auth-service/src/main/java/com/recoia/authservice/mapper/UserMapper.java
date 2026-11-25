package com.recoia.authservice.mapper;

import com.recoia.authservice.dto.RegisterRequest;
import com.recoia.authservice.dto.UserResponse;
import com.recoia.authservice.model.User;
import com.recoia.authservice.model.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // request : ce que le client a envoye (email)
    // passwordHash et role : decides par le service, pas par le client
    public User toEntity(RegisterRequest request, String passwordHash, Role role) {
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordHash);
        user.setRole(role);
        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getRole());
    }
}
