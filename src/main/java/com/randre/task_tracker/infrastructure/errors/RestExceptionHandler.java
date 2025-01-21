package com.randre.task_tracker.infrastructure.errors;

import com.randre.task_tracker.exceptions.AccessDeniedException;
import com.randre.task_tracker.exceptions.TaskNotFoundException;
import com.randre.task_tracker.exceptions.UserAlreadyExistsException;
import com.randre.task_tracker.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestExceptionMessage httpMessageNotReadableHandler(HttpMessageNotReadableException e) {

        return new RestExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            String objectName = error instanceof FieldError
                    ? ((FieldError) error).getField()
                    : error.getObjectName();

            errors.put(objectName, errorMessage);
        });

        return errors;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestExceptionMessage badCredentialsHandler(BadCredentialsException e) {

        return new RestExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestExceptionMessage taskNotFoundHandler(TaskNotFoundException e) {

        return new RestExceptionMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestExceptionMessage accessDeniedHandler(AccessDeniedException e) {

        return new RestExceptionMessage(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestExceptionMessage userAlreadyExistHandler(UserAlreadyExistsException e) {

        return new RestExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestExceptionMessage userNotFoundException(UserNotFoundException e) {

        return new RestExceptionMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestExceptionMessage authorizationDeniedException(AuthorizationDeniedException e) {

        return new RestExceptionMessage(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }
}
