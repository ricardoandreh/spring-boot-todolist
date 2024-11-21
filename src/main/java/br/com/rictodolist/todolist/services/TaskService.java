package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.dtos.TaskRequestDTO;
import br.com.rictodolist.todolist.dtos.TaskUpdateDTO;
import br.com.rictodolist.todolist.exceptions.AccessDeniedException;
import br.com.rictodolist.todolist.exceptions.TaskNotFoundException;
import br.com.rictodolist.todolist.models.TaskModel;
import br.com.rictodolist.todolist.repositories.ITaskRepository;
import br.com.rictodolist.todolist.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository taskRepository;

    public TaskModel create(TaskRequestDTO taskRequestDto, UUID idUser) {
        TaskModel taskModel = new TaskModel();

        BeanUtils.copyProperties(taskRequestDto, taskModel);
        taskModel.setIdUser(idUser);

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

    public List<TaskModel> getAll(UUID idUser) {
        return this.taskRepository.findByIdUser(idUser);
    }

    public TaskModel update(TaskUpdateDTO taskUpdateDto, UUID idTask, UUID idUser) {
        TaskModel task = this.taskRepository.findById(idTask).orElse(null);

        if (task == null) {
            throw new TaskNotFoundException();
        }

        if (!task.getIdUser().equals(idUser)) {
            throw new AccessDeniedException("Usuário não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(taskUpdateDto, task);

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
