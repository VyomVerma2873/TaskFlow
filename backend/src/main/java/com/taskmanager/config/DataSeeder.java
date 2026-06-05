package com.taskmanager.config;

import com.taskmanager.entity.User;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User demoUser = User.builder()
                        .username("demo")
                        .email("demo@demo.com")
                        .password(passwordEncoder.encode("demo123"))
                        .build();
                userRepository.save(demoUser);
                log.info("=== Demo user created: email=demo@demo.com, password=demo123 ===");
            }
        };
    }
}
