package com.randre.task_tracker.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank String username,
        @Size(max = 30, message = "O nome deve possuir no máximo 30 caracteres") String name,
        @NotBlank String password) {
}
