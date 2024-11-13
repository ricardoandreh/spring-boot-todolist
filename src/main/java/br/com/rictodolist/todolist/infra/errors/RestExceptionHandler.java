package br.com.rictodolist.todolist.infra.errors;

import br.com.rictodolist.todolist.exceptions.AccessDeniedException;
import br.com.rictodolist.todolist.exceptions.DateValidationException;
import br.com.rictodolist.todolist.exceptions.TaskNotFoundException;
import br.com.rictodolist.todolist.exceptions.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestExceptionMessage> httpMessageNotReadableHandler(HttpMessageNotReadableException e) {
        RestExceptionMessage threatResponse = new RestExceptionMessage(HttpStatus.BAD_REQUEST, e.getMostSpecificCause().getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
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

    @ExceptionHandler(DateValidationException.class)
    public ResponseEntity<RestExceptionMessage> dateValidationHandler(DateValidationException e) {
        RestExceptionMessage threatResponse = new RestExceptionMessage(HttpStatus.BAD_REQUEST, e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<RestExceptionMessage> userAlreadyExistHandler(UserAlreadyExistException e) {
        RestExceptionMessage threatResponse = new RestExceptionMessage(HttpStatus.BAD_REQUEST, e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
    }
}
