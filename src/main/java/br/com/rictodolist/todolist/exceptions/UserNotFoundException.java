package br.com.rictodolist.todolist.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Usuário não encontrado");
    }

    public UserNotFoundException(String username) {
        super("Usuário não encontrado: " + username);
    }
}
