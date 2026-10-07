package com.example.hospital.config;

import com.example.hospital.model.AppUser;
import com.example.hospital.model.Role;
import com.example.hospital.repository.RoleRepository;
import com.example.hospital.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed-password:demo}")
    private String seedPassword;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            // Create roles
            Role receptionist = getOrCreate("ROLE_RECEPTIONIST");
            Role nurse        = getOrCreate("ROLE_NURSE");
            Role doctor       = getOrCreate("ROLE_DOCTOR");

            // Seed users
            seedUser("anna",  Set.of(receptionist));
            seedUser("brian", Set.of(nurse));
            seedUser("clara", Set.of(doctor));
            seedUser("david", Set.of(nurse, doctor)); // multi-role — no special casing
        };
    }

    private Role getOrCreate(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(new Role(null, name)));
    }

    private void seedUser(String username, Set<Role> roles) {
        if (userRepository.findByUsername(username).isEmpty()) {
            userRepository.save(new AppUser(null, username, passwordEncoder.encode(seedPassword), roles));
        }
    }
}
