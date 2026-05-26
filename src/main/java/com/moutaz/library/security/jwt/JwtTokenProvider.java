package com.moutaz.library.security.jwt;

import com.moutaz.library.user.entities.Role;
import com.moutaz.library.user.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JWT Token Provider for generating and validating JWT tokens.
 * Provides functionality for token generation, validation, and claims extraction.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user the user for whom to generate the token
     * @return the generated JWT token
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return Optional containing the username if extraction is successful, empty otherwise
     */
    public Optional<String> getUsernameFromToken(String token) {
        try {
            Claims claims = getClaims(token);
            return Optional.ofNullable(claims.getSubject());
        } catch (Exception ex) {
            log.error("Failed to extract username from token");
            return Optional.empty();
        }
    }

    /**
     * Extracts the user ID from the given JWT token.
     *
     * @param token the JWT token
     * @return Optional containing the user ID if extraction is successful, empty otherwise
     */
    public Optional<Long> getUserIdFromToken(String token) {
        try {
            Claims claims = getClaims(token);
            Integer userId = claims.get("userId", Integer.class);
            return Optional.ofNullable(userId).map(Long::valueOf);
        } catch (Exception ex) {
            log.error("Failed to extract user ID from token");
            return Optional.empty();
        }
    }

    /**
     * Extracts the roles from the given JWT token.
     *
     * @param token the JWT token
     * @return Optional containing the list of roles if extraction is successful, empty otherwise
     */
    @SuppressWarnings("unchecked")
    public Optional<List<String>> getRolesFromToken(String token) {
        try {
            Claims claims = getClaims(token);
            List<String> roles = claims.get("roles", List.class);
            return Optional.ofNullable(roles);
        } catch (Exception ex) {
            log.error("Failed to extract roles from token");
            return Optional.empty();
        }
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token
     * @return the claims
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Gets the token expiration time in milliseconds.
     *
     * @return the expiration time in milliseconds
     */
    public long getExpirationMs() {
        return expirationMs;
    }
}
