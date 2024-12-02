package br.com.rictodolist.todolist.exceptions;

import br.com.rictodolist.todolist.constants.ErrorMessages;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super(ErrorMessages.USER_NOT_FOUND);
    }

    public UserNotFoundException(String username) {
        super(String.format("%s: ", ErrorMessages.USER_NOT_FOUND) + username);
    }
}
