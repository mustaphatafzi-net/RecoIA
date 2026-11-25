package com.recoia.authservice.service.impl;

import com.recoia.authservice.dto.RegisterRequest;
import com.recoia.authservice.dto.UserResponse;
import com.recoia.authservice.exception.EmailAlreadyUsedException;
import com.recoia.authservice.mapper.UserMapper;
import com.recoia.authservice.model.User;
import com.recoia.authservice.model.enums.Role;
import com.recoia.authservice.repository.UserRepository;
import com.recoia.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(RegisterRequest request) {
        // 1. l'email est-il deja pris ?
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyUsedException(request.email());
        }

        // 2. chiffrer le mot de passe
        String passwordHash = passwordEncoder.encode(request.password());

        // 3. fabriquer le User : le role est TOUJOURS STUDENT ici
        User user = userMapper.toEntity(request, passwordHash, Role.STUDENT);

        // 4. sauvegarder, puis 5. repondre sans le mot de passe
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }
}
