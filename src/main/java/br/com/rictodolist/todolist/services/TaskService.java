package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.dtos.task.TaskPaginationDTO;
import br.com.rictodolist.todolist.dtos.task.TaskRequestDTO;
import br.com.rictodolist.todolist.dtos.task.TaskResponseDTO;
import br.com.rictodolist.todolist.dtos.task.TaskUpdateDTO;
import br.com.rictodolist.todolist.exceptions.AccessDeniedException;
import br.com.rictodolist.todolist.exceptions.TaskNotFoundException;
import br.com.rictodolist.todolist.mappers.TaskMapper;
import br.com.rictodolist.todolist.models.TaskModel;
import br.com.rictodolist.todolist.models.UserModel;
import br.com.rictodolist.todolist.repositories.ITaskRepository;
import br.com.rictodolist.todolist.repositories.IUserRepository;
import br.com.rictodolist.todolist.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    public TaskResponseDTO create(TaskRequestDTO taskRequestDto) {
        TaskModel taskModel = new TaskModel();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        BeanUtils.copyProperties(taskRequestDto, taskModel);
        taskModel.setUser((UserModel) user);

        TaskModel taskCreated = this.taskRepository.save(taskModel);

        return this.taskMapper.toDTO(taskCreated);
    }

    public TaskResponseDTO getOne(UUID id) {
        TaskModel task = this.taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!task.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Usuário não tem permissão para visualizar essa tarefa");
        }

        return this.taskMapper.toDTO(task);
    }

    public TaskPaginationDTO getAll(int page, int size, String sortBy, boolean ascending) {
        Pageable pageable = this.getPageable(page, size, sortBy, ascending);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<TaskModel> tasks = this.taskRepository.findByUserUsername(username, pageable);

        return this.taskMapper.toPaginationDTO(tasks, pageable, sortBy, ascending);
    }

    public TaskResponseDTO update(TaskUpdateDTO taskUpdateDto, UUID id) {
        TaskModel task = this.taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!task.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Usuário não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(taskUpdateDto, task);

        TaskModel taskCreated = this.taskRepository.save(task);

        return this.taskMapper.toDTO(taskCreated);
    }

    public TaskResponseDTO delete(UUID id) {
        TaskModel task = this.taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!task.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Usuário não tem permissão para remover essa tarefa");
        }

        this.taskRepository.delete(task);

        return this.taskMapper.toDTO(task);
    }

    public Pageable getPageable(int page, int size, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }
}
