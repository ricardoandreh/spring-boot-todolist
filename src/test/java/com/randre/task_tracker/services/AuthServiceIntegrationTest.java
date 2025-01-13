package com.randre.task_tracker.services;

import com.randre.task_tracker.dtos.user.UserRequestDTO;
import com.randre.task_tracker.exceptions.UserNotFoundException;
import com.randre.task_tracker.infrastructure.security.SecurityConfig;
import com.randre.task_tracker.repositories.IUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private SecurityConfig securityConfig;

    @MockBean
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should throw UserNotFoundException when user is not found")
    public void whenUserNotFound_thenThrowUserNotFoundException() {
        UserRequestDTO userRequestDto = new UserRequestDTO(
                "nonExistentUser",
                null,
                "rawPassword"
        );

        when(this.userRepository.findByUsername(userRequestDto.username()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                InternalAuthenticationServiceException.class,
                () -> {
                    authService.login(userRequestDto);
                });

        assertInstanceOf(UserNotFoundException.class, exception.getCause());
        assertEquals(
                "Usuário não encontrado: " + userRequestDto.username(),
                exception.getCause().getMessage());

        verify(userRepository, times(1))
                .findByUsername(anyString());
    }
}
