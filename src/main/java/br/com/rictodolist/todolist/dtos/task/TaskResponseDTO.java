package br.com.rictodolist.todolist.dtos.task;

import org.springframework.hateoas.Link;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponseDTO(
        UUID id,
        String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String priority,
        LocalDateTime createdAt,
        Link links) {
}
