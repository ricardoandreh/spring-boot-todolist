package com.randre.task_tracker.dtos.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefreshRequestDTO(@NotNull @NotBlank String refreshToken) {
}
