package br.com.rictodolist.todolist.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotNull @NotBlank String username,
        @Size(max = 30, message = "O nome deve possuir no máximo 30 caracteres") String name,
        @NotNull @NotBlank String password) {
}
