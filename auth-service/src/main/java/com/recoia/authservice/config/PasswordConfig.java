package com.recoia.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean //fabrique un objet que tu garderas, et que tu pourras donner à qui en a besoin
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
