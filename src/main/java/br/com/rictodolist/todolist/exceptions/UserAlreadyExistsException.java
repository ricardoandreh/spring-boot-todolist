package br.com.rictodolist.todolist.exceptions;

import br.com.rictodolist.todolist.constants.ErrorMessages;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super(ErrorMessages.USER_ALREADY_EXISTS);
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
