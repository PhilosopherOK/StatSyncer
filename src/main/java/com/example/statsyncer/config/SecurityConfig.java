package com.example.statsyncer.config;

import com.example.statsyncer.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфігурація безпеки для Spring Security.
 * Налаштовує фільтри, авторизацію, управління сесіями та обробку помилок.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // JWT-фільтр для перевірки токенів
    private final AuthenticationProvider authenticationProvider; // Провайдер аутентифікації
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint; // Кастомний обробник помилок

    /**
     * Налаштовує SecurityFilterChain для управління HTTP-безпекою.
     *
     * @param http Об'єкт HttpSecurity для конфігурації.
     * @return SecurityFilterChain для роботи в додатку.
     * @throws Exception У разі помилок конфігурації.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Відключає CSRF, оскільки додаток використовує JWT.
                .csrf()
                .disable()

                // Налаштовує доступ до маршрутів.
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**") // Відкритий доступ для маршрутів авторизації.
                .permitAll()
                .anyRequest() // Всі інші запити вимагають авторизації.
                .authenticated()

                .and()
                // Налаштовує сесії як безстанні (stateless).
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                // Додає кастомний провайдер аутентифікації.
                .authenticationProvider(authenticationProvider)

                // Додає JWT-фільтр перед стандартним UsernamePasswordAuthenticationFilter.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                // Встановлює кастомний обробник помилок аутентифікації.
                .authenticationEntryPoint(customAuthenticationEntryPoint);

        return http.build();
    }
}

