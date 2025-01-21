package com.randre.task_tracker.services;

import com.randre.task_tracker.dtos.jwt.TokenResponseDTO;
import com.randre.task_tracker.dtos.jwt.RefreshRequestDTO;
import com.randre.task_tracker.dtos.user.LoginRequestDTO;
import com.randre.task_tracker.dtos.user.RegisterRequestDTO;
import com.randre.task_tracker.dtos.user.UserResponseDTO;
import com.randre.task_tracker.exceptions.UserAlreadyExistsException;
import com.randre.task_tracker.exceptions.UserNotFoundException;
import com.randre.task_tracker.infrastructure.enums.Role;
import com.randre.task_tracker.mappers.UserMapper;
import com.randre.task_tracker.models.UserModel;
import com.randre.task_tracker.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
@RequiredArgsConstructor
class AuthServiceTest {

    private final MessageSource messageSourceInjected;

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
    private RegisterRequestDTO registerRequestDto;
    private LoginRequestDTO loginRequestDto;

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

        this.registerRequestDto = new RegisterRequestDTO(
                baseUser.getUsername(),
                null,
                baseUser.getPassword()
        );
        this.loginRequestDto = new LoginRequestDTO(
                baseUser.getUsername(),
                baseUser.getPassword()
        );
    }

    @Test
    @DisplayName("Should register user successfully when everything is OK")
    void registerUserSuccessfully() {
        when(this.userRepository.findByUsername(this.registerRequestDto.username()))
                .thenReturn(Optional.empty());
        when(this.passwordEncoder.encode(this.registerRequestDto.password()))
                .thenReturn("encodedPassword");
        when(this.userRepository.save(any(UserModel.class)))
                .thenReturn(this.baseUser);

        UserResponseDTO user = this.authService.register(registerRequestDto);

        assertNotNull(user);

        verify(this.userRepository, times(1))
                .findByUsername(this.registerRequestDto.username());
        verify(this.passwordEncoder, times(1))
                .encode(this.registerRequestDto.password());
        verify(this.userRepository, times(1))
                .save(any(UserModel.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistException")
    void registerUserUserAlreadyExistException() {
        UserModel existingUser = this.baseUser;

        when(this.userRepository.findByUsername(this.registerRequestDto.username()))
                .thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistsException.class, () ->
                this.authService.register(this.registerRequestDto));

        verify(this.userRepository, times(1))
                .findByUsername(this.registerRequestDto.username());
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

        TokenResponseDTO tokenResponseDto = this.authService.login(this.loginRequestDto);

        assertNotNull(tokenResponseDto);
        assertEquals("access-token", tokenResponseDto.access());
        assertEquals("refresh-token", tokenResponseDto.refresh());

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
        LoginRequestDTO userWithBadCredentials = new LoginRequestDTO(
                "user",
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
                .thenThrow(new UserNotFoundException(this.messageSourceInjected, this.registerRequestDto.username()));

        assertThrows(UserNotFoundException.class, () ->
                this.authService.login(this.loginRequestDto));

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
                .thenReturn(this.registerRequestDto.username());
        when(this.userRepository.findByUsername(this.registerRequestDto.username()))
                .thenReturn(Optional.of(this.baseUser));
        when(this.jwtService.generateAccessToken(this.baseUser))
                .thenReturn("access-token");

        assertEquals("access-token", this.authService.refresh(refreshRequestDto).access());

        verify(this.jwtService, times(1))
                .validateToken("invalid-refresh-token");
        verify(this.userRepository, times(1))
                .findByUsername(this.registerRequestDto.username());
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
                .thenThrow(new UserNotFoundException(this.messageSourceInjected));

        assertThrows(UserNotFoundException.class, () ->
                this.authService.refresh(refreshRequestDto));

        verify(this.jwtService, times(1))
                .validateToken("invalid-refresh-token");
        verify(this.userRepository, times(1))
                .findByUsername(this.registerRequestDto.username());
        verify(this.jwtService, never())
                .generateAccessToken(any(UserModel.class));
    }

    @Test
    @DisplayName("Should enconde password when creating new user")
    void registerUserHashesPassword() {
        when(this.userRepository.findByUsername(this.registerRequestDto.username()))
                .thenReturn(Optional.empty());
        when(this.passwordEncoder.encode(this.registerRequestDto.password()))
                .thenReturn("encodedPassword");
        when(this.userRepository.save(any(UserModel.class)))
                .thenReturn(this.baseUser);

        this.authService.register(this.registerRequestDto);

        verify(this.passwordEncoder, times(1))
                .encode(this.registerRequestDto.password());
        verify(this.userRepository, times(1))
                .save(any(UserModel.class));
    }
}
