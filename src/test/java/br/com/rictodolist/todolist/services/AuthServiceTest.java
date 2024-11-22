package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.config.security.Role;
import br.com.rictodolist.todolist.dtos.UserRequestDTO;
import br.com.rictodolist.todolist.exceptions.UserAlreadyExistException;
import br.com.rictodolist.todolist.models.UserModel;
import br.com.rictodolist.todolist.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create user successfully when everything is OK")
    void createUserCase1() {
        UserModel userModel = new UserModel(
                UUID.fromString("211a2efd-2e07-405c-9c53-94c5e8eab964"),
                "user",
                null,
                "user",
                LocalDateTime.now(),
                Role.USER, null
        );

        UserRequestDTO userRequestDto = new UserRequestDTO(
                userModel.getUsername(),
                null,
                userModel.getPassword()
        );

        when(this.userRepository.findByUsername(userRequestDto.username())).thenReturn(null); // O Usuário ainda não existe
        when(this.userRepository.save(any(UserModel.class))).thenReturn(userModel);

        this.authService.register(userRequestDto);

        verify(this.userRepository, times(1)).findByUsername(userRequestDto.username());
        verify(this.userRepository, times(1)).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistException")
    void createUserCase2() {
        UserModel existingUser = new UserModel(
                UUID.fromString("211a2efd-2e07-405c-9c53-94c5e8eab964"),
                "user",
                null,
                "user",
                LocalDateTime.now(),
                Role.USER, null
        );

        UserRequestDTO userRequestDto = new UserRequestDTO(
                existingUser.getUsername(),
                null,
                existingUser.getPassword()
        );

        when(this.userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistException.class, () -> this.authService.register(userRequestDto));

        verify(this.userRepository, times(1)).findByUsername(userRequestDto.username());
        verify(this.userRepository, never()).save(any(UserModel.class));
    }
}
