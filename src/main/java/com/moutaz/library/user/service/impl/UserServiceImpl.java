package com.moutaz.library.user.service.impl;

import com.moutaz.library.exception.custom.RoleNotFoundException;
import com.moutaz.library.exception.custom.UserAlreadyExistsException;
import com.moutaz.library.user.entities.Role;
import com.moutaz.library.user.entities.User;
import com.moutaz.library.user.repository.RoleRepository;
import com.moutaz.library.user.repository.UserRepository;
import com.moutaz.library.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(String username, String email, String rawPassword, Set<String> roleNames) {
        // Validate user doesn't already exist
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username '" + username + "' already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email '" + email + "' already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        // Hash password with BCrypt
        user.setPassword(passwordEncoder.encode(rawPassword));

        // Assign roles
        Set<Role> roles = new HashSet<>();

        // If no roles specified, assign default ROLE_USER
        if (roleNames == null || roleNames.isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RoleNotFoundException("Default role 'ROLE_USER' not found"));
            roles.add(userRole);
        } else {
            // Find and assign specified roles
            for (String roleName : roleNames) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException("Role '" + roleName + "' not found"));
                roles.add(role);
            }
        }

        user.setRoles(roles);

        // Save and return user
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
