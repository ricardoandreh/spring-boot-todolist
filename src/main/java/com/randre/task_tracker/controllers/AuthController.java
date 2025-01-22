package com.randre.task_tracker.controllers;

import com.randre.task_tracker.dtos.jwt.AccessResponseDTO;
import com.randre.task_tracker.dtos.jwt.TokenResponseDTO;
import com.randre.task_tracker.dtos.jwt.RefreshRequestDTO;
import com.randre.task_tracker.dtos.user.RegisterRequestDTO;
import com.randre.task_tracker.dtos.user.LoginRequestDTO;
import com.randre.task_tracker.dtos.user.UserResponseDTO;
import com.randre.task_tracker.services.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody @Valid RegisterRequestDTO user) {

        return this.authService.register(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDTO login(@RequestBody @Valid LoginRequestDTO user) {

        return this.authService.login(user);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public AccessResponseDTO refresh(@RequestBody @Valid RefreshRequestDTO refresh) {

        return this.authService.refresh(refresh);
    }
}
