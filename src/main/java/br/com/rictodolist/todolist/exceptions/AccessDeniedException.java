package br.com.rictodolist.todolist.exceptions;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("Usuário não possui permissão");
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
