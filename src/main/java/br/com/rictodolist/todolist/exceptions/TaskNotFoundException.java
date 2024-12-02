package br.com.rictodolist.todolist.exceptions;

import br.com.rictodolist.todolist.constants.ErrorMessages;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException() {
        super(ErrorMessages.TASK_NOT_FOUND);
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
