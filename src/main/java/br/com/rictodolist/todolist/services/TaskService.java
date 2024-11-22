package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.dtos.TaskRequestDTO;
import br.com.rictodolist.todolist.dtos.TaskUpdateDTO;
import br.com.rictodolist.todolist.exceptions.AccessDeniedException;
import br.com.rictodolist.todolist.exceptions.TaskNotFoundException;
import br.com.rictodolist.todolist.models.TaskModel;
import br.com.rictodolist.todolist.repositories.ITaskRepository;
import br.com.rictodolist.todolist.repositories.IUserRepository;
import br.com.rictodolist.todolist.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IUserRepository userRepository;

    public TaskModel create(TaskRequestDTO taskRequestDto) {
        TaskModel taskModel = new TaskModel();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        BeanUtils.copyProperties(taskRequestDto, taskModel);
        taskModel.setUser(user);

        return this.taskRepository.save(taskModel);
    }

    public TaskModel getOne(UUID id) {
        TaskModel task = this.taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!task.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Usuário não tem permissão para visualizar essa tarefa");
        }

        return task;
    }

    public List<TaskModel> getAll(UUID idUser) {
        return this.taskRepository.findByIdUser(idUser);
    }

    public TaskModel update(TaskUpdateDTO taskUpdateDto, UUID id) {
        TaskModel task = this.taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!task.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Usuário não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(taskUpdateDto, task);

        return this.taskRepository.save(task);
    }

    public TaskModel delete(UUID id) {
        TaskModel task = this.taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!task.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Usuário não tem permissão para remover essa tarefa");
        }

        this.taskRepository.delete(task);

        return task;
    }
}
