package com.randre.task_tracker.services;

import com.randre.task_tracker.dtos.jwt.AccessResponseDTO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    private final IUserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    private final MessageSource messageSource;

    public UserResponseDTO register(RegisterRequestDTO registerRequestDto) {
        this.userRepository
                .findByUsername(registerRequestDto.username())
                .ifPresent((userDetails) -> {
                    throw new UserAlreadyExistsException(this.messageSource);
                });

        String encodedPassword = this.passwordEncoder.encode(registerRequestDto.password());

        UserModel userModel = new UserModel();

        BeanUtils.copyProperties(registerRequestDto, userModel);
        userModel.setPassword(encodedPassword);
        userModel.setRole(Role.USER);

        UserModel userCreated = this.userRepository.save(userModel);

        return this.userMapper.toDTO(userCreated);
    }

    public TokenResponseDTO login(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        UserModel user = (UserModel) auth.getPrincipal();

        String token = this.jwtService.generateAccessToken(user);
        String refreshToken = this.jwtService.generateAccessToken(user);

        return new TokenResponseDTO(token, refreshToken);
    }

    public AccessResponseDTO refresh(RefreshRequestDTO refreshRequestDTO) {
        String refreshToken = refreshRequestDTO.refreshToken();

        String username = this.jwtService.validateToken(refreshToken);

        UserModel user = (UserModel) this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(this.messageSource, username));

        String newAccessToken = this.jwtService.generateRefreshToken(user);

        return new AccessResponseDTO(newAccessToken);
    }
}
