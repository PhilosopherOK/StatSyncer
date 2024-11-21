package com.example.statsyncer.services;

import com.example.statsyncer.models.User;
import com.example.statsyncer.repositories.UserRepository;
import com.example.statsyncer.requests.AuthRequest;
import com.example.statsyncer.responses.AuthResponse;
import com.example.statsyncer.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Метод для реєстрації нового користувача
    public AuthResponse register(AuthRequest request) {
        if (request.getPassword().isBlank()
                || request.getPassword().length() > 32
                || request.getPassword().length() < 8) {
            throw new IllegalArgumentException("The password must be at least 8 characters and no more than 32");
        }
        User user = new User(request.getEmail(),
                passwordEncoder.encode(request.getPassword()), Role.USER);
        try {
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);
            return new AuthResponse(jwtToken);
        } catch (Exception e) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    // Метод для аутентифікації користувача
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));
        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}

