package br.com.rictodolist.todolist.exceptions;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        super("Usuário já existe");
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
