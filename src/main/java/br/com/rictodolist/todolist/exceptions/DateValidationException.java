package br.com.rictodolist.todolist.exceptions;

public class DateValidationException extends RuntimeException {

    public DateValidationException() {
        super("A data não está formatada corretamente");
    }

    public DateValidationException(String message) {
        super(message);
    }
}
