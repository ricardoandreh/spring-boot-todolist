package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.domain.task.TaskModel;
import br.com.rictodolist.todolist.exceptions.AccessDeniedException;
import br.com.rictodolist.todolist.exceptions.DateValidationException;
import br.com.rictodolist.todolist.exceptions.TaskNotFoundException;
import br.com.rictodolist.todolist.repositories.ITaskRepository;
import br.com.rictodolist.todolist.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository taskRepository;

    public TaskModel create(TaskModel taskModel, UUID id) {
        taskModel.setIdUser(id);

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            throw new DateValidationException("A data de início / data de término deve ser maior do que a data atual");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            throw new DateValidationException("A data de início deve ser menor do que a data de término");
        }

        return this.taskRepository.save(taskModel);
    }

    public TaskModel getOne(UUID idTask, UUID idUser) {
        TaskModel task = this.taskRepository.findById(idTask).orElse(null);

        if (task == null) {
            throw new TaskNotFoundException();
        }

        if (!task.getIdUser().equals(idUser)) {
            throw new AccessDeniedException("Usuário não tem permissão para visualizar essa tarefa");
        }

        return task;
    }

    public List<TaskModel> getAll(UUID id) {
        return this.taskRepository.findByIdUser(id);
    }

    public TaskModel update(TaskModel taskModel, UUID idTask, UUID idUser) {
        TaskModel task = this.taskRepository.findById(idTask).orElse(null);

        if (task == null) {
            throw new TaskNotFoundException();
        }

        if (!task.getIdUser().equals(idUser)) {
            throw new AccessDeniedException("Usuário não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(taskModel, task);

        return this.taskRepository.save(task);
    }

    public TaskModel delete(UUID idTask, UUID idUser) {
        TaskModel task = this.taskRepository.findById(idTask).orElse(null);

        if (task == null) {
            throw new TaskNotFoundException();
        }

        if (!task.getIdUser().equals(idUser)) {
            throw new AccessDeniedException("Usuário não tem permissão para remover essa tarefa");
        }

        this.taskRepository.delete(task);

        return task;
    }
}
