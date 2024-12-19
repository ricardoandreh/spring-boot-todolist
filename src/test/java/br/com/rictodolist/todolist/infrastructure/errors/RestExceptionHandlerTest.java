package br.com.rictodolist.todolist.infrastructure.errors;

import br.com.rictodolist.todolist.exceptions.AccessDeniedException;
import br.com.rictodolist.todolist.exceptions.TaskNotFoundException;
import br.com.rictodolist.todolist.exceptions.UserAlreadyExistsException;
import br.com.rictodolist.todolist.exceptions.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RestExceptionHandlerTest {

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Autowired
    private MessageSource messageSource;

    @Test
    @DisplayName("Should handle UserNotFoundException")
    void handleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException(this.messageSource);

        RestExceptionMessage response = restExceptionHandler.userNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Usuário não encontrado", response.getMessage());
    }

    @Test
    @DisplayName("Should handle UserNotFoundException providing username")
    void handleUserNotFoundExceptionProvidingUsername() {
        UserNotFoundException exception = new UserNotFoundException(this.messageSource, "unknown");

        RestExceptionMessage response = restExceptionHandler.userNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Usuário não encontrado: unknown", response.getMessage());
    }

    @Test
    @DisplayName("Should handle BadCredentialsException")
    void handleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        RestExceptionMessage response = restExceptionHandler.badCredentialsHandler(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Invalid credentials", response.getMessage());
    }

    @Test
    @DisplayName("Should handle AccessDeniedException")
    void handleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException(this.messageSource);

        RestExceptionMessage response = restExceptionHandler.accessDeniedHandler(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatus());
        assertEquals("Usuário não possui permissão", response.getMessage());
    }

    @Test
    @DisplayName("Should handle AccessDeniedException with custom message")
    void handleAccessDeniedExceptionCustomMessage() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        RestExceptionMessage response = restExceptionHandler.accessDeniedHandler(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatus());
        assertEquals("Access denied", response.getMessage());
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException")
    void handleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid JSON format");

        RestExceptionMessage response = restExceptionHandler.httpMessageNotReadableHandler(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Invalid JSON format", response.getMessage());
    }

    @Test
    @DisplayName("Should handle UserAlreadyExistsException")
    void handleUserAlreadyExistsException() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException(this.messageSource);

        RestExceptionMessage response = restExceptionHandler.userAlreadyExistHandler(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Usuário já existe", response.getMessage());
    }

    @Test
    @DisplayName("Should handle TaskNotFoundException")
    void handleTaskNotFoundException() {
        TaskNotFoundException exception = new TaskNotFoundException(this.messageSource);

        RestExceptionMessage response = restExceptionHandler.taskNotFoundHandler(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Tarefa não encontrada", response.getMessage());
    }
}
