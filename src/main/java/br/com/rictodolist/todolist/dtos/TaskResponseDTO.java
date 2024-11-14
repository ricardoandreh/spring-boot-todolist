package br.com.rictodolist.todolist.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponseDTO(
        UUID id,
        String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String priority,
        LocalDateTime createdAt) {
}
