package br.com.rictodolist.todolist.dtos.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String name,
        LocalDateTime createdAt) {
}
