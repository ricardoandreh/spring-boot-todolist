package com.randre.task_tracker.services;

import com.randre.task_tracker.dtos.user.LoginRequestDTO;
import com.randre.task_tracker.exceptions.UserNotFoundException;
import com.randre.task_tracker.infrastructure.security.SecurityConfig;
import com.randre.task_tracker.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class AuthServiceIntegrationTest {

    private final AuthService authService;

    private final SecurityConfig securityConfig;

    private final PasswordEncoder passwordEncoder;

    @Mock
    private IUserRepository userRepository;

    @Test
    @DisplayName("Should throw UserNotFoundException when user is not found")
    public void whenUserNotFound_thenThrowUserNotFoundException() {
        LoginRequestDTO loginRequestDto = new LoginRequestDTO(
                "nonExistentUser",
                "rawPassword"
        );

        when(this.userRepository.findByUsername(loginRequestDto.username()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                InternalAuthenticationServiceException.class,
                () -> {
                    authService.login(loginRequestDto);
                });

        assertInstanceOf(UserNotFoundException.class, exception.getCause());
        assertEquals(
                "Usuário não encontrado: " + loginRequestDto.username(),
                exception.getCause().getMessage());
    }
}
