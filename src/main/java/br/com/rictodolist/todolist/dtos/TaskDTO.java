package br.com.rictodolist.todolist.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDTO(String title, String description, LocalDateTime startAt, LocalDateTime endAt, String priority, UUID idUser, LocalDateTime createdAt) {}
