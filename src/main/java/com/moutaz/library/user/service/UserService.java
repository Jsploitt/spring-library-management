package com.moutaz.library.user.service;

import com.moutaz.library.user.entities.User;

import java.util.Optional;
import java.util.Set;

public interface UserService {

    /**
     * Creates a new user with the provided details and assigns roles.
     *
     * @param username the username
     * @param email the email address
     * @param rawPassword the raw password (will be hashed)
     * @param roleNames the set of role names to assign to the user
     * @return the created user
     * @throws com.moutaz.library.exception.custom.UserAlreadyExistsException if username or email already exists
     * @throws com.moutaz.library.exception.custom.RoleNotFoundException if any of the specified roles don't exist
     */
    User createUser(String username, String email, String rawPassword, Set<String> roleNames);

    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return an Optional containing the user if found, or empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by email.
     *
     * @param email the email address
     * @return an Optional containing the user if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given username exists.
     *
     * @param username the username
     * @return true if user exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email address
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}
