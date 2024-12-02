package br.com.rictodolist.todolist.infrastructure.errors;

import br.com.rictodolist.todolist.exceptions.AccessDeniedException;
import br.com.rictodolist.todolist.exceptions.TaskNotFoundException;
import br.com.rictodolist.todolist.exceptions.UserAlreadyExistException;
import br.com.rictodolist.todolist.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestExceptionMessage> httpMessageNotReadableHandler(HttpMessageNotReadableException e) {
        RestExceptionMessage threatResponse = new RestExceptionMessage(HttpStatus.BAD_REQUEST, e.getMostSpecificCause().getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            } else {
                String objectName = error.getObjectName();
                String errorMessage = error.getDefaultMessage();
                errors.put(objectName, errorMessage);
            }
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<RestExceptionMessage> taskNotFoundHandler(TaskNotFoundException e) {
        RestExceptionMessage threatResponse = new RestExceptionMessage(HttpStatus.NOT_FOUND, e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestExceptionMessage> accessDeniedHandler(AccessDeniedException e) {
        RestExceptionMessage threatResponse = new RestExceptionMessage(HttpStatus.FORBIDDEN, e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(threatResponse);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<RestExceptionMessage> userAlreadyExistHandler(UserAlreadyExistException e) {
        RestExceptionMessage threatResponse = new RestExceptionMessage(HttpStatus.BAD_REQUEST, e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestExceptionMessage> userNotFoundException(UserNotFoundException e) {
        RestExceptionMessage threatResponse = new RestExceptionMessage(HttpStatus.BAD_REQUEST, e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
    }
}
