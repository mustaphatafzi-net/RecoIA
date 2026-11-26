package com.recoia.authservice.service.impl;

import com.recoia.authservice.dto.AuthResponse;
import com.recoia.authservice.dto.LoginRequest;
import com.recoia.authservice.dto.RegisterRequest;
import com.recoia.authservice.dto.UserResponse;
import com.recoia.authservice.exception.EmailAlreadyUsedException;
import com.recoia.authservice.exception.InvalidCredentialsException;
import com.recoia.authservice.mapper.UserMapper;
import com.recoia.authservice.model.User;
import com.recoia.authservice.model.enums.Role;
import com.recoia.authservice.repository.UserRepository;
import com.recoia.authservice.service.AuthService;
import com.recoia.authservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Inscription publique : le role est TOUJOURS STUDENT
    @Override
    public UserResponse register(RegisterRequest request) {
        return createUser(request, Role.STUDENT);
    }

    // Reserve aux admins (la verification se fait dans le controller)
    @Override
    public UserResponse createAdmin(RegisterRequest request) {
        return createUser(request, Role.ADMIN);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. chercher l'utilisateur par email
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        // 2. comparer le mot de passe saisi avec le hash de la base
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        // 3. un compte desactive ne peut pas se connecter
        if (!user.getEnabled()) {
            throw new InvalidCredentialsException();
        }

        // 4. memoriser la date de cette connexion
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // 5. fabriquer le token et repondre
        String token = jwtService.generateToken(user);
        return userMapper.toAuthResponse(user, token);
    }

    // Travail commun a register et createAdmin : seul le role change
    private UserResponse createUser(RegisterRequest request, Role role) {
        // 1. l'email est-il deja pris ?
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyUsedException(request.email());
        }

        // 2. chiffrer le mot de passe
        String passwordHash = passwordEncoder.encode(request.password());

        // 3. fabriquer le User avec le role recu
        User user = userMapper.toEntity(request, passwordHash, role);

        // 4. sauvegarder, puis 5. repondre sans le mot de passe
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }
}
