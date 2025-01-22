package com.randre.task_tracker.annotations.validations;

import com.randre.task_tracker.utils.ErrorMessages;
import com.randre.task_tracker.annotations.validators.FutureDateValidator;
import jakarta.validation.Constraint;
import org.springframework.context.PayloadApplicationEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureDateValidator.class)
public @interface FutureDate {

    String message() default ErrorMessages.FUTURE_DATE;

    Class<?>[] groups() default {};

    Class<? extends PayloadApplicationEvent>[] payload() default {};
}
