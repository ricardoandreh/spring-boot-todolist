package com.randre.task_tracker.services;

import com.randre.task_tracker.infrastructure.enums.Role;
import com.randre.task_tracker.models.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.randre.task_tracker.services.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    private final String secret = "my-secret-key";

    private UserModel user;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(this.jwtService, "secret", this.secret);

        this.user = new UserModel(
                UUID.randomUUID(),
                "user",
                null,
                "rawPassword",
                LocalDateTime.now(),
                Role.USER,
                null
        );
    }

    @Test
    @DisplayName("Should generate a valid access token")
    void generateAccessToken() {
        String token = this.jwtService.generateAccessToken(this.user);

        assertNotNull(token);

        String subject = this.jwtService.validateToken(token);

        assertEquals(this.user.getUsername(), subject);
    }

    @Test
    @DisplayName("Should generate a valid refresh token")
    void generateRefreshToken() {
        String token = this.jwtService.generateRefreshToken(this.user);

        assertNotNull(token);

        String subject = this.jwtService.validateToken(token);

        assertEquals(this.user.getUsername(), subject);
    }

    @Test
    @DisplayName("Should throw JWTCreationException on tokens generation error")
    void generateTokenWithError() {
        try (MockedStatic<Algorithm> mockedAlgorithm = Mockito.mockStatic(Algorithm.class)) {
            mockedAlgorithm.when(() -> Algorithm.HMAC256(secret))
                    .thenThrow(new JWTCreationException("Error creating token", null));

            RuntimeException accessException = assertThrows(
                    RuntimeException.class,
                    () -> this.jwtService.generateRefreshToken(user)
            );

            RuntimeException refreshException = assertThrows(
                    RuntimeException.class,
                    () -> this.jwtService.generateAccessToken(user)
            );

            assertTrue(accessException.getMessage().contains("Error while generating token"));
            assertTrue(refreshException.getMessage().contains("Error while generating token"));
        }
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if the secret is empty for refresh token")
    void generateTokenWithNullSecret() {
        ReflectionTestUtils.setField(this.jwtService, "secret", null);

        IllegalArgumentException accessException = assertThrows(IllegalArgumentException.class,
                () -> this.jwtService.generateAccessToken(this.user));

        IllegalArgumentException refreshException = assertThrows(IllegalArgumentException.class,
                () -> this.jwtService.generateRefreshToken(this.user));

        assertEquals("The Secret cannot be null or empty", accessException.getMessage());
        assertEquals("The Secret cannot be null or empty", refreshException.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if the secret is empty for refresh token")
    void generateTokenWithEmptySecret() {
        ReflectionTestUtils.setField(this.jwtService, "secret", "");

        IllegalArgumentException accessException = assertThrows(IllegalArgumentException.class,
                () -> this.jwtService.generateAccessToken(this.user));

        IllegalArgumentException refreshException = assertThrows(IllegalArgumentException.class,
                () -> this.jwtService.generateRefreshToken(this.user));

        assertEquals("The Secret cannot be null or empty", accessException.getMessage());
        assertEquals("The Secret cannot be null or empty", refreshException.getMessage());
    }

    @Test
    @DisplayName("Should return empty string for expired token")
    void validateExpiredToken() {
        Instant expiredInstant = Instant.now().minusSeconds(60 * 60);

        String expiredToken = this.jwtService.generateAccessToken(this.user);

        expiredToken = this.addExpirationToToken(expiredToken, expiredInstant);

        String subject = this.jwtService.validateToken(expiredToken);

        assertEquals("", subject);
    }

    private String addExpirationToToken(String token, Instant expirationTime) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expirationDate = Date.from(expirationTime);

        return JWT.create()
                .withSubject(decodedJWT.getSubject())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(this.secret));
    }
}
