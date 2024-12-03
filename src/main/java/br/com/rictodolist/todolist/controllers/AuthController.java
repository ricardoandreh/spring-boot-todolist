package br.com.rictodolist.todolist.controllers;

import br.com.rictodolist.todolist.dtos.jwt.AccessResponseDTO;
import br.com.rictodolist.todolist.dtos.jwt.LoginResponseDTO;
import br.com.rictodolist.todolist.dtos.jwt.RefreshRequestDTO;
import br.com.rictodolist.todolist.dtos.user.*;
import br.com.rictodolist.todolist.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserResponseDTO register(@RequestBody @Valid UserRequestDTO user) {

        return this.authService.register(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LoginResponseDTO login(@RequestBody @Valid UserRequestDTO user) {

        return this.authService.login(user);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccessResponseDTO refresh(@RequestBody @Valid RefreshRequestDTO refresh) {

        return this.authService.refresh(refresh);
    }
}
