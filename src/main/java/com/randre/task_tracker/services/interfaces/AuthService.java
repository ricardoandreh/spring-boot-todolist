package com.randre.task_tracker.services.interfaces;

import com.randre.task_tracker.dtos.jwt.AccessResponseDTO;
import com.randre.task_tracker.dtos.jwt.RefreshRequestDTO;
import com.randre.task_tracker.dtos.jwt.TokenResponseDTO;
import com.randre.task_tracker.dtos.user.LoginRequestDTO;
import com.randre.task_tracker.dtos.user.RegisterRequestDTO;
import com.randre.task_tracker.dtos.user.UserResponseDTO;

public interface AuthService {

    UserResponseDTO register(RegisterRequestDTO registerRequestDto);

    TokenResponseDTO login(LoginRequestDTO loginRequestDTO);

    AccessResponseDTO refresh(RefreshRequestDTO refreshRequestDTO);
}
