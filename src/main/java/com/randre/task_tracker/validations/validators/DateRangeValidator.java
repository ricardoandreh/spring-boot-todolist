package com.randre.task_tracker.validations.validators;

import com.randre.task_tracker.constants.ErrorMessages;
import com.randre.task_tracker.validations.annotations.DateRange;
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
            Class<?> cls = value.getClass();
            Field startDate = cls.getDeclaredField(startDateField);
            Field endDate = cls.getDeclaredField(endDateField);

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
                    context.buildConstraintViolationWithTemplate(ErrorMessages.DATE_RANGE)
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
