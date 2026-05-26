package com.moutaz.library.auth.service.impl;

import com.moutaz.library.auth.dto.AuthResponse;
import com.moutaz.library.auth.dto.LoginRequest;
import com.moutaz.library.auth.dto.RegisterRequest;
import com.moutaz.library.auth.service.AuthService;
import com.moutaz.library.security.jwt.JwtTokenProvider;
import com.moutaz.library.user.entities.Role;
import com.moutaz.library.user.entities.User;
import com.moutaz.library.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Implementation of AuthService for handling user registration and authentication.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        // Create user with ROLE_USER by default
        // UserService will validate that username/email are not taken
        User user = userService.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                Collections.singleton("ROLE_USER")
        );

        // Generate JWT token for immediate login
        String token = jwtTokenProvider.generateToken(user);

        log.info("User registered successfully: {}", user.getUsername());

        return buildAuthResponse(token, user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt: {}", request.getUsername());

        // Authenticate using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Load user details
        User user = (User) authentication.getPrincipal();

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user);

        log.info("User logged in successfully: {}", user.getUsername());

        return buildAuthResponse(token, user);
    }

    /**
     * Helper method to build AuthResponse from token and user.
     */
    private AuthResponse buildAuthResponse(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
