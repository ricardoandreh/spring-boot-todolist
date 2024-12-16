package br.com.rictodolist.todolist.dtos.task;

import br.com.rictodolist.todolist.infrastructure.enums.Priority;
import org.springframework.hateoas.Link;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponseDTO(
        UUID id,
        String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Priority priority,
        LocalDateTime createdAt,
        Link links) {
}
