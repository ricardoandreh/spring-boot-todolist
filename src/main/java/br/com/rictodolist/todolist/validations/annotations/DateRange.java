package br.com.rictodolist.todolist.validations.annotations;

import br.com.rictodolist.todolist.validations.validators.DateRangeValidator;
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

    String message() default "A data de início deve ser menor que a data de término.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
