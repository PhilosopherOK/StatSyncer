package com.example.statsyncer.controllers;


import com.example.statsyncer.requests.AuthRequest;
import com.example.statsyncer.responses.AuthResponse;
import com.example.statsyncer.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контролер для обробки запитів, пов'язаних з аутентифікацією та реєстрацією користувачів.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService; // Сервіс для обробки логіки аутентифікації

    /**
     * Реєстрація нового користувача.
     *
     * @param request Об'єкт із даними для реєстрації (наприклад, email, пароль).
     * @return Відповідь із даними аутентифікації.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Аутентифікація користувача (логін).
     *
     * @param request Об'єкт із даними для аутентифікації (наприклад, email, пароль).
     * @return Відповідь із JWT-токеном або повідомленням про успішну аутентифікацію.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}

