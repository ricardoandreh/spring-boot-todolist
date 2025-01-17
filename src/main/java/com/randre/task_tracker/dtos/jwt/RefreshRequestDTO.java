package com.randre.task_tracker.dtos.jwt;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(@NotBlank String refreshToken) {
}
