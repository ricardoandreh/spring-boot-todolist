package br.com.rictodolist.todolist.dtos.task;

import br.com.rictodolist.todolist.validations.annotations.DateRange;
import br.com.rictodolist.todolist.validations.annotations.FutureDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@DateRange(startDateField = "startAt", endDateField = "endAt")
public record TaskRequestDTO(
        @NotNull @NotBlank @Size(max = 50, message = "O título deve conter no máximo 50 caracteres") String title,
        @NotNull @NotBlank String description,
        @NotNull @FutureDate LocalDateTime startAt,
        @NotNull @FutureDate LocalDateTime endAt,
        @NotNull @NotBlank String priority) {
}
