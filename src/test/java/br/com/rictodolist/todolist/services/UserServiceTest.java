package br.com.rictodolist.todolist.services;

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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
                LocalDateTime.now()
        );

        UserRequestDTO userRequestDto = new UserRequestDTO(
                userModel.getUsername(),
                null,
                userModel.getPassword()
        );

        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(null); // O Usuário ainda não existe
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        this.userService.create(userRequestDto);

        verify(userRepository, times(1)).findByUsername(userRequestDto.username());
        verify(userRepository, times(1)).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistException")
    void createUserCase2() {
        UserModel existingUser = new UserModel(
                UUID.fromString("211a2efd-2e07-405c-9c53-94c5e8eab964"),
                "user",
                null,
                "user",
                LocalDateTime.now()
        );

        UserRequestDTO userRequestDto = new UserRequestDTO(
                existingUser.getUsername(),
                null,
                existingUser.getPassword()
        );

        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(existingUser);

        assertThrows(UserAlreadyExistException.class, () -> userService.create(userRequestDto));

        verify(userRepository, times(1)).findByUsername(userRequestDto.username());
        verify(userRepository, never()).save(any(UserModel.class));
    }
}
