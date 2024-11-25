package br.com.rictodolist.todolist.dtos.task;

import br.com.rictodolist.todolist.validations.annotations.DateRange;
import br.com.rictodolist.todolist.validations.annotations.FutureDate;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@DateRange(startDateField = "startAt", endDateField = "endAt")
public record TaskUpdateDTO(
        @Size(max = 50, message = "O título deve conter no máximo 50 caracteres") String title,
        String description,
        @FutureDate LocalDateTime startAt,
        @FutureDate LocalDateTime endAt,
        String priority) {
}
