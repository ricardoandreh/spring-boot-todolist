package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.dtos.TaskRequestDTO;
import br.com.rictodolist.todolist.dtos.TaskResponseDTO;
import br.com.rictodolist.todolist.dtos.TaskUpdateDTO;
import br.com.rictodolist.todolist.exceptions.AccessDeniedException;
import br.com.rictodolist.todolist.exceptions.DateValidationException;
import br.com.rictodolist.todolist.exceptions.TaskNotFoundException;
import br.com.rictodolist.todolist.models.TaskModel;
import br.com.rictodolist.todolist.repositories.ITaskRepository;
import br.com.rictodolist.todolist.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository taskRepository;

    public TaskResponseDTO create(TaskRequestDTO taskRequestDto, UUID id) {
        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskRequestDto.startAt()) || currentDate.isAfter(taskRequestDto.endAt())) {
            throw new DateValidationException("A data de início / data de término deve ser maior do que a data atual");
        }

        if (taskRequestDto.startAt().isAfter(taskRequestDto.endAt())) {
            throw new DateValidationException("A data de início deve ser menor do que a data de término");
        }

        TaskModel taskModel = new TaskModel();

        BeanUtils.copyProperties(taskRequestDto, taskModel);
        taskModel.setIdUser(id);

        TaskModel taskCreated = this.taskRepository.save(taskModel);

        return new TaskResponseDTO(
                taskCreated.getId(),
                taskCreated.getTitle(),
                taskCreated.getDescription(),
                taskCreated.getStartAt(),
                taskCreated.getEndAt(),
                taskCreated.getPriority(),
                taskCreated.getCreatedAt()
        );
    }

    public TaskResponseDTO getOne(UUID idTask, UUID idUser) {
        TaskModel task = this.taskRepository.findById(idTask).orElse(null);

        if (task == null) {
            throw new TaskNotFoundException();
        }

        if (!task.getIdUser().equals(idUser)) {
            throw new AccessDeniedException("Usuário não tem permissão para visualizar essa tarefa");
        }

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStartAt(),
                task.getEndAt(),
                task.getPriority(),
                task.getCreatedAt()
        );
    }

    public List<TaskResponseDTO> getAll(UUID id) {
        List<TaskModel> tasks = this.taskRepository.findByIdUser(id);

        List<TaskResponseDTO> taskResponseDtos = new ArrayList<>();

        for (TaskModel task : tasks) {
            taskResponseDtos.add(new TaskResponseDTO(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStartAt(),
                    task.getEndAt(),
                    task.getPriority(),
                    task.getCreatedAt()
            ));
        }

        return taskResponseDtos;
    }

    public TaskResponseDTO update(TaskUpdateDTO taskUpdateDto, UUID idTask, UUID idUser) {
        TaskModel task = this.taskRepository.findById(idTask).orElse(null);

        if (task == null) {
            throw new TaskNotFoundException();
        }

        if (!task.getIdUser().equals(idUser)) {
            throw new AccessDeniedException("Usuário não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(taskUpdateDto, task);

        TaskModel taskUpdated = this.taskRepository.save(task);

        return new TaskResponseDTO(
                taskUpdated.getId(),
                taskUpdated.getTitle(),
                taskUpdated.getDescription(),
                taskUpdated.getStartAt(),
                taskUpdated.getEndAt(),
                taskUpdated.getPriority(),
                taskUpdated.getCreatedAt()
        );
    }

    public TaskResponseDTO delete(UUID idTask, UUID idUser) {
        TaskModel task = this.taskRepository.findById(idTask).orElse(null);

        if (task == null) {
            throw new TaskNotFoundException();
        }

        if (!task.getIdUser().equals(idUser)) {
            throw new AccessDeniedException("Usuário não tem permissão para remover essa tarefa");
        }

        this.taskRepository.delete(task);

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStartAt(),
                task.getEndAt(),
                task.getPriority(),
                task.getCreatedAt()
        );
    }
}
