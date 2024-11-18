package br.com.rictodolist.todolist.controllers;

import br.com.rictodolist.todolist.dtos.UserRequestDTO;
import br.com.rictodolist.todolist.dtos.UserResponseDTO;
import br.com.rictodolist.todolist.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO user) {
        UserResponseDTO userCreated = this.userService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}