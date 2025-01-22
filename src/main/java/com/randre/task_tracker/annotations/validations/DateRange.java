package com.randre.task_tracker.annotations.validations;

import com.randre.task_tracker.utils.ErrorMessages;
import com.randre.task_tracker.annotations.validators.DateRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface DateRange {

    String message() default ErrorMessages.DATE_RANGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String startDateField();

    String endDateField();
}
