package br.com.rictodolist.todolist.dtos.task;

import br.com.rictodolist.todolist.infrastructure.enums.Priority;
import br.com.rictodolist.todolist.validations.annotations.DateRange;
import br.com.rictodolist.todolist.validations.annotations.FutureDate;
import br.com.rictodolist.todolist.validations.annotations.NullOrNotBlank;
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
