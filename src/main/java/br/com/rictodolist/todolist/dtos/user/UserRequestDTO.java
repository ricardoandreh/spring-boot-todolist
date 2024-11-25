package br.com.rictodolist.todolist.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotNull @NotBlank String username,
        String name,
        @NotNull @NotBlank String password) {
}
