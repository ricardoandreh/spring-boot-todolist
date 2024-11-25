package br.com.rictodolist.todolist.validations.validators;

import br.com.rictodolist.todolist.validations.annotations.DateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDateField();
        this.endDateField = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            Class<?> clazz = value.getClass();
            Field startDate = clazz.getDeclaredField(startDateField);
            Field endDate = clazz.getDeclaredField(endDateField);

            startDate.setAccessible(true);
            endDate.setAccessible(true);

            Object startValue = startDate.get(value);
            Object endValue = endDate.get(value);

            if (startValue == null || endValue == null) {
                return true;
            }

            if (startValue instanceof LocalDateTime && endValue instanceof LocalDateTime) {
                boolean isValid = !((LocalDateTime) endValue).isBefore((LocalDateTime) startValue);

                if (!isValid) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("A data de término deve ser maior ou igual à data de início.")
                            .addPropertyNode("endAt")
                            .addConstraintViolation();
                }

                return isValid;
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Erro ao acessar os campos especificados na anotação @DateRange.", e);
        }

        return false;
    }
}
