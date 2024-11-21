package com.example.statsyncer.config;

import com.example.statsyncer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * Конфігураційний клас для налаштування компонентів безпеки в додатку.
 */
@Configuration
@RequiredArgsConstructor

public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Налаштовує службу для отримання інформації про користувача за його email.
     * @return UserDetailsService, який шукає користувача в базі даних за email.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            return userRepository.findUserByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }

    /**
     * Налаштовує AuthenticationProvider для використання кастомної служби UserDetailsService
     * та шифрування паролів за допомогою BCrypt.
     * @return AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Налаштовує AuthenticationManager для керування аутентифікацією користувачів.
     * @param configuration поточна конфігурація аутентифікації.
     * @return AuthenticationManager.
     * @throws Exception якщо виникає помилка при отриманні AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Налаштовує енкодер для шифрування паролів за допомогою BCrypt.
     * @return PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


