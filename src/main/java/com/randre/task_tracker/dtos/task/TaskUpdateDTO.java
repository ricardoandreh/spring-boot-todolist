package com.randre.task_tracker.dtos.task;

import com.randre.task_tracker.infrastructure.enums.Priority;
import com.randre.task_tracker.annotations.validations.DateRange;
import com.randre.task_tracker.annotations.validations.FutureDate;
import com.randre.task_tracker.annotations.validations.NullOrNotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@DateRange(startDateField = "startAt", endDateField = "endAt")
public record TaskUpdateDTO(
        @NullOrNotBlank @Size(max = 50, message = "{error-messages.maximum-title-size}") String title,
        @NullOrNotBlank String description,
        @FutureDate LocalDateTime startAt,
        @FutureDate LocalDateTime endAt,
        Priority priority) {
}
