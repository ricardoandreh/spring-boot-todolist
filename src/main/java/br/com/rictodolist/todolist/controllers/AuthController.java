package br.com.rictodolist.todolist.controllers;

import br.com.rictodolist.todolist.dtos.LoginResponseDTO;
import br.com.rictodolist.todolist.dtos.UserRequestDTO;
import br.com.rictodolist.todolist.dtos.UserResponseDTO;
import br.com.rictodolist.todolist.models.UserModel;
import br.com.rictodolist.todolist.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO user) {
        UserModel userCreated = this.authService.register(user);

        UserResponseDTO userResponseDto = new UserResponseDTO(
                userCreated.getId(),
                userCreated.getUsername(),
                userCreated.getName(),
                userCreated.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody UserRequestDTO user) {
        String token = this.authService.login(user);

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token));
    }
}
