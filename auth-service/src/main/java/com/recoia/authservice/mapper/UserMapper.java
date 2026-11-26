package com.recoia.authservice.mapper;

import com.recoia.authservice.dto.AuthResponse;
import com.recoia.authservice.dto.RegisterRequest;
import com.recoia.authservice.dto.UserResponse;
import com.recoia.authservice.model.User;
import com.recoia.authservice.model.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // request : ce que le client a envoye (prenom, nom, email)
    // passwordHash et role : decides par le service, pas par le client
    public User toEntity(RegisterRequest request, String passwordHash, Role role) {
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPasswordHash(passwordHash);
        user.setRole(role);
        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole());
    }

    // token : fabrique par le service (JwtService)
    public AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse(
                token,
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole());
    }
}
