package br.com.rictodolist.todolist.exceptions;

import br.com.rictodolist.todolist.constants.ErrorMessages;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super(ErrorMessages.ACCESS_DENIED);
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
