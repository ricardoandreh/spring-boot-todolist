package com.randre.task_tracker.dtos.task;

import com.randre.task_tracker.infrastructure.enums.Priority;
import com.randre.task_tracker.validations.annotations.DateRange;
import com.randre.task_tracker.validations.annotations.FutureDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@DateRange(startDateField = "startAt", endDateField = "endAt")
public record TaskRequestDTO(
        @NotBlank @Size(max = 50, message = "{error-messages.maximum-title-size}") String title,
        @NotBlank String description,
        @NotNull @FutureDate LocalDateTime startAt,
        @NotNull @FutureDate LocalDateTime endAt,
        @NotNull Priority priority) {
}
