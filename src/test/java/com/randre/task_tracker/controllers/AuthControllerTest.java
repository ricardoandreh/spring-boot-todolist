package com.randre.task_tracker.controllers;

import com.randre.task_tracker.dtos.jwt.AccessResponseDTO;
import com.randre.task_tracker.dtos.jwt.LoginResponseDTO;
import com.randre.task_tracker.dtos.jwt.RefreshRequestDTO;
import com.randre.task_tracker.dtos.user.UserRequestDTO;
import com.randre.task_tracker.dtos.user.UserResponseDTO;
import com.randre.task_tracker.repositories.IUserRepository;
import com.randre.task_tracker.services.AuthService;
import com.randre.task_tracker.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private IUserRepository userRepository;

    private UserResponseDTO baseUserResponse;
    private LoginResponseDTO baseLoginResponse;
    private AccessResponseDTO baseAccessResponse;

    @BeforeEach
    void setUp() {
        this.baseUserResponse = new UserResponseDTO(
                UUID.randomUUID(),
                "user",
                null,
                LocalDateTime.now()
        );
        this.baseLoginResponse = new LoginResponseDTO(
                "access-token",
                "refresh-token"
        );
        this.baseAccessResponse = new AccessResponseDTO(
                "new-access-token"
        );
    }

    @Test
    @DisplayName("Should register a new user successfully")
    void registerUser() throws Exception {
        when(this.authService.register(any(UserRequestDTO.class)))
                .thenReturn(this.baseUserResponse);
        when(this.userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "username": "user",
                                        "password": "rawPassword"
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("Should authenticate a user and return tokens")
    void loginUser() throws Exception {
        when(this.authService.login(any(UserRequestDTO.class)))
                .thenReturn(baseLoginResponse);

        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "username": "user",
                                        "password": "rawPassowrd"
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.access").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    @DisplayName("Should refresh the access token using a valid refresh token")
    void refreshToken() throws Exception {
        when(this.authService.refresh(any(RefreshRequestDTO.class)))
                .thenReturn(this.baseAccessResponse);

        this.mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "refreshToken": "refresh-token"
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.access").value("new-access-token"));
    }

    @Test
    @DisplayName("Should throw MethodArgumentNotValidException when username is null validation fails")
    void registerUserUsernameNull() throws Exception {
        when(this.authService.register(any(UserRequestDTO.class)))
                .thenReturn(this.baseUserResponse);
        when(this.userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "password": "rawPassword"
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should throw MethodArgumentNotValidException when username is blank validation fails")
    void registerUserUsernameBlank() throws Exception {
        when(this.authService.register(any(UserRequestDTO.class)))
                .thenReturn(this.baseUserResponse);
        when(this.userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "username": "",
                                        "password": "rawPassword"
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("must not be blank"));
    }

    @Test
    @DisplayName("Should throw MethodArgumentNotValidException when password is null validation fails")
    void registerUserPasswordNull() throws Exception {
        when(this.authService.register(any(UserRequestDTO.class)))
                .thenReturn(this.baseUserResponse);
        when(this.userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "username": "user"
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should throw MethodArgumentNotValidException when password is blank validation fails")
    void registerUserPasswordBlank() throws Exception {
        when(this.authService.register(any(UserRequestDTO.class)))
                .thenReturn(this.baseUserResponse);
        when(this.userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "username": "user",
                                        "password": ""
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.password").value("must not be blank"));
    }

    @Test
    @DisplayName("Should throw MethodArgumentNotValidException when name sizing validation fails")
    void registerUserNameSizing() throws Exception {
        when(this.authService.register(any(UserRequestDTO.class)))
                .thenReturn(this.baseUserResponse);
        when(this.userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "username": "user",
                                        "name": "nome bem longo aaaaaaaaaaaaaaaa",
                                        "password": "rawPassword"
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("O nome deve possuir no máximo 30 caracteres"));
    }
}
