package br.com.rictodolist.todolist.validations.validators;

import br.com.rictodolist.todolist.dtos.TaskRequestDTO;
import br.com.rictodolist.todolist.validations.annotations.DateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<DateRange, TaskRequestDTO> {

    @Override
    public boolean isValid(TaskRequestDTO value, ConstraintValidatorContext context) {
        if (value == null || value.startAt() == null || value.endAt() == null) {
            return true;
        }

        boolean isValid = !value.endAt().isBefore(value.startAt());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("A data de término deve ser maior ou igual à data de início.")
                    .addPropertyNode("endAt")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
