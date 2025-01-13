package com.randre.task_tracker.services;

import com.randre.task_tracker.dtos.jwt.LoginResponseDTO;
import com.randre.task_tracker.dtos.jwt.RefreshRequestDTO;
import com.randre.task_tracker.dtos.user.UserRequestDTO;
import com.randre.task_tracker.dtos.user.UserResponseDTO;
import com.randre.task_tracker.exceptions.UserAlreadyExistsException;
import com.randre.task_tracker.exceptions.UserNotFoundException;
import com.randre.task_tracker.infrastructure.enums.Role;
import com.randre.task_tracker.mappers.UserMapper;
import com.randre.task_tracker.models.UserModel;
import com.randre.task_tracker.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private MessageSource messageSourceAutowired;

    @InjectMocks
    private AuthService authService;

    @Spy
    private UserMapper userMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private UserModel baseUser;
    private UserRequestDTO userRequestDto;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        this.baseUser = new UserModel(
                UUID.randomUUID(),
                "user",
                null,
                "rawPassword",
                LocalDateTime.now(),
                Role.USER,
                null
        );

        this.userRequestDto = new UserRequestDTO(
                baseUser.getUsername(),
                null,
                baseUser.getPassword()
        );
    }

    @Test
    @DisplayName("Should register user successfully when everything is OK")
    void registerUserSuccessfully() {
        when(this.userRepository.findByUsername(this.userRequestDto.username()))
                .thenReturn(Optional.empty());
        when(this.passwordEncoder.encode(this.userRequestDto.password()))
                .thenReturn("encodedPassword");
        when(this.userRepository.save(any(UserModel.class)))
                .thenReturn(this.baseUser);

        UserResponseDTO user = this.authService.register(userRequestDto);

        assertNotNull(user);

        verify(this.userRepository, times(1))
                .findByUsername(this.userRequestDto.username());
        verify(this.passwordEncoder, times(1))
                .encode(this.userRequestDto.password());
        verify(this.userRepository, times(1))
                .save(any(UserModel.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistException")
    void registerUserUserAlreadyExistException() {
        UserModel existingUser = this.baseUser;

        when(this.userRepository.findByUsername(this.userRequestDto.username()))
                .thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistsException.class, () ->
                this.authService.register(this.userRequestDto));

        verify(this.userRepository, times(1))
                .findByUsername(this.userRequestDto.username());
        verify(this.userRepository, never())
                .save(any(UserModel.class));
    }

    @Test
    @DisplayName("Should login user successfully when everything is OK")
    void loginUserSuccessfully() {
        Authentication mockAuthentication = mock(Authentication.class);

        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal())
                .thenReturn(this.baseUser);
        when(this.jwtService.generateAccessToken(this.baseUser))
                .thenReturn("access-token");
        when(this.jwtService.generateRefreshToken(this.baseUser))
                .thenReturn("refresh-token");

        LoginResponseDTO loginResponseDto = this.authService.login(this.userRequestDto);

        assertNotNull(loginResponseDto);
        assertEquals("access-token", loginResponseDto.access());
        assertEquals("refresh-token", loginResponseDto.refreshToken());

        verify(this.authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(this.jwtService, times(1))
                .generateAccessToken(this.baseUser);
        verify(this.jwtService, times(1))
                .generateRefreshToken(this.baseUser);
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when authentication fails")
    void loginUserBadCredentialsException() {
        UserRequestDTO userWithBadCredentials = new UserRequestDTO(
                "user",
                null,
                "wrongPassword"
        );

        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () ->
                this.authService.login(userWithBadCredentials));

        verify(this.authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(this.jwtService, never())
                .generateAccessToken(any(UserModel.class));
        verify(this.jwtService, never())
                .generateRefreshToken(any(UserModel.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException")
    void loginUserUserNotFoundException() {
        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UserNotFoundException(this.messageSourceAutowired, this.userRequestDto.username()));

        assertThrows(UserNotFoundException.class, () ->
                this.authService.login(this.userRequestDto));

        verify(this.authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(this.jwtService, never())
                .generateAccessToken(any(UserModel.class));
        verify(this.jwtService, never())
                .generateRefreshToken(any(UserModel.class));
    }

    @Test
    @DisplayName("Should login user successfully when everything is OK")
    void refreshUserSuccessfully() {
        RefreshRequestDTO refreshRequestDto = new RefreshRequestDTO("invalid-refresh-token");

        when(this.jwtService.validateToken(refreshRequestDto.refreshToken()))
                .thenReturn(this.userRequestDto.username());
        when(this.userRepository.findByUsername(this.userRequestDto.username()))
                .thenReturn(Optional.of(this.baseUser));
        when(this.jwtService.generateAccessToken(this.baseUser))
                .thenReturn("access-token");

        assertEquals("access-token", this.authService.refresh(refreshRequestDto).access());

        verify(this.jwtService, times(1))
                .validateToken("invalid-refresh-token");
        verify(this.userRepository, times(1))
                .findByUsername(this.userRequestDto.username());
        verify(this.jwtService, times(1))
                .generateAccessToken(any(UserModel.class));
    }

    @Test
    @DisplayName("Should throw JWTVerificationException when authentication fails")
    void refreshUserJWTVerificationException() {
        RefreshRequestDTO refreshRequestDto = new RefreshRequestDTO("invalid-refresh-token");

        when(this.jwtService.validateToken(refreshRequestDto.refreshToken()))
                .thenReturn("user");
        when(this.userRepository.findByUsername("user"))
                .thenThrow(new UserNotFoundException(this.messageSourceAutowired));

        assertThrows(UserNotFoundException.class, () ->
                this.authService.refresh(refreshRequestDto));

        verify(this.jwtService, times(1))
                .validateToken("invalid-refresh-token");
        verify(this.userRepository, times(1))
                .findByUsername(this.userRequestDto.username());
        verify(this.jwtService, never())
                .generateAccessToken(any(UserModel.class));
    }

    @Test
    @DisplayName("Should enconde password when creating new user")
    void registerUserHashesPassword() {
        when(this.userRepository.findByUsername(this.userRequestDto.username()))
                .thenReturn(Optional.empty());
        when(this.passwordEncoder.encode(this.userRequestDto.password()))
                .thenReturn("encodedPassword");
        when(this.userRepository.save(any(UserModel.class)))
                .thenReturn(this.baseUser);

        this.authService.register(this.userRequestDto);

        verify(this.passwordEncoder, times(1))
                .encode(this.userRequestDto.password());
        verify(this.userRepository, times(1))
                .save(any(UserModel.class));
    }
}
