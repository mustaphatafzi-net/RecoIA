package com.recoia.authservice.config;

import com.recoia.authservice.dto.RegisterRequest;
import com.recoia.authservice.mapper.UserMapper;
import com.recoia.authservice.model.User;
import com.recoia.authservice.model.enums.Role;
import com.recoia.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Ces deux valeurs viennent du config-server (auth-service.yml)
    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        // Si l'admin existe deja, on ne touche a rien
        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Compte admin deja present : {}", adminEmail);
            return;
        }

        RegisterRequest request = new RegisterRequest("Admin", "RecoIA", adminEmail, adminPassword);
        String passwordHash = passwordEncoder.encode(adminPassword);

        User admin = userMapper.toEntity(request, passwordHash, Role.ADMIN);
        userRepository.save(admin);

        log.info("Compte admin cree : {}", adminEmail);
    }
}
