package br.com.rictodolist.todolist.exceptions;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("{error-messages.access-denied}");
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
