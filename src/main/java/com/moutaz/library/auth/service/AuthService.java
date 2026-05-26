package com.moutaz.library.auth.service;

import com.moutaz.library.auth.dto.AuthResponse;
import com.moutaz.library.auth.dto.LoginRequest;
import com.moutaz.library.auth.dto.RegisterRequest;

public interface AuthService {

    /**
     * Registers a new user with the provided details.
     * Creates a new user account with ROLE_USER by default and returns an authentication token.
     *
     * @param request the registration request containing username, email, and password
     * @return AuthResponse containing the JWT token and user information
     * @throws com.moutaz.library.exception.custom.UserAlreadyExistsException if username or email already exists
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates a user with username and password.
     * Validates credentials and returns an authentication token upon success.
     *
     * @param request the login request containing username and password
     * @return AuthResponse containing the JWT token and user information
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials are invalid
     */
    AuthResponse login(LoginRequest request);
}
