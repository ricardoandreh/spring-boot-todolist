package com.randre.task_tracker.dtos.task;

import com.randre.task_tracker.infrastructure.enums.Priority;
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
