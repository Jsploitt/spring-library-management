package com.moutaz.library.security.config;

import com.moutaz.library.security.jwt.JwtAuthenticationEntryPoint;
import com.moutaz.library.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the library management application.
 * Configures JWT-based authentication and authorization.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;

    /**
     * Configures the security filter chain with JWT authentication and authorization rules.
     *
     * @param http HttpSecurity configuration
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless API
                .csrf(csrf -> csrf.disable())

                // Configure stateless session management
                .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                    // Public endpoints - authentication not required
                    .requestMatchers("/api/v1/auth/**").permitAll()

                    // Admin-only endpoints
                    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                    // Book management endpoints - role-based access
                    .requestMatchers(HttpMethod.POST, "/api/v1/books/**").hasAnyRole("ADMIN", "LIBRARIAN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasAnyRole("ADMIN", "LIBRARIAN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/books/**").hasAnyRole("ADMIN", "LIBRARIAN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/books/**").hasAnyRole("ADMIN", "LIBRARIAN", "USER")

                    // All other requests require authentication
                    .anyRequest().authenticated()
                )

                // Configure exception handling
                .exceptionHandling(ex ->
                    ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                // Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the authentication manager for login authentication.
     *
     * @param authConfig authentication configuration
     * @return AuthenticationManager
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the password encoder using BCrypt.
     *
     * @return PasswordEncoder implementation
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication provider with UserDetailsService and PasswordEncoder.
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
