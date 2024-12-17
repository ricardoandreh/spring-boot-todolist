package br.com.rictodolist.todolist.dtos.task;

import br.com.rictodolist.todolist.infrastructure.enums.Priority;
import br.com.rictodolist.todolist.validations.annotations.DateRange;
import br.com.rictodolist.todolist.validations.annotations.FutureDate;
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
