package br.com.rictodolist.todolist.dtos;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskUpdateDTO(
        @Size(max = 50, message = "O título deve conter no máximo 50 caracteres") String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String priority) {
}
