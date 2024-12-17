package br.com.rictodolist.todolist.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("{error-messages.user-already-exists}");
    }
}
