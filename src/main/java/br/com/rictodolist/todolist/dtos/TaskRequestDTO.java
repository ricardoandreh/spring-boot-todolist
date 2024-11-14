package br.com.rictodolist.todolist.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskRequestDTO(
        @NotNull @NotBlank @Size(max = 50, message = "O título deve conter no máximo 50 caracteres") String title,
        @NotNull @NotBlank String description,
        @NotNull LocalDateTime startAt,
        @NotNull LocalDateTime endAt,
        @NotNull @NotBlank String priority) {
}
