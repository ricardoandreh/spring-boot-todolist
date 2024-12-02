package br.com.rictodolist.todolist.infrastructure.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class RestExceptionMessage {

    private HttpStatus status;
    private String message;
}
