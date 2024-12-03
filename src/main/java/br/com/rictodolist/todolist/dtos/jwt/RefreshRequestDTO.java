package br.com.rictodolist.todolist.dtos.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefreshRequestDTO(@NotNull @NotBlank String refreshToken) {
}
