package com.example.hospital.config;

import com.example.hospital.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppUserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var appUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            var authorities = appUser.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            return new User(appUser.getUsername(), appUser.getPassword(), authorities);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // H2 console
                        .requestMatchers("/h2-console/**").permitAll()
                        // Access rules — most specific paths first
                        .requestMatchers(HttpMethod.POST, "/patients/*/prescriptions").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/patients/*/vitals").hasAnyRole("NURSE", "DOCTOR")
                        .requestMatchers(HttpMethod.GET,  "/patients/*").hasAnyRole("NURSE", "DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/patients").hasRole("RECEPTIONIST")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                // Allow H2 console frames
                .headers(headers -> headers.frameOptions(fo -> fo.sameOrigin()));

        return http.build();
    }
}
