package br.com.rictodolist.todolist.exceptions;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException() {
        super("{error-messages.task-not-found}");
    }
}
