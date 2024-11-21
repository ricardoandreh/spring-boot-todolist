package br.com.rictodolist.todolist.dtos;

import br.com.rictodolist.todolist.validations.annotations.DateRange;
import br.com.rictodolist.todolist.validations.annotations.FutureDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@DateRange
public record TaskUpdateDTO(
        @NotBlank @Size(max = 50, message = "O título deve conter no máximo 50 caracteres") String title,
        @NotBlank String description,
        @FutureDate LocalDateTime startAt,
        @FutureDate LocalDateTime endAt,
        @NotBlank String priority) {
}

