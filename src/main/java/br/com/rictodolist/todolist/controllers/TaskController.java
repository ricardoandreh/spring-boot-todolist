package br.com.rictodolist.todolist.controllers;

import br.com.rictodolist.todolist.dtos.task.TaskRequestDTO;
import br.com.rictodolist.todolist.dtos.task.TaskResponseDTO;
import br.com.rictodolist.todolist.dtos.task.TaskUpdateDTO;
import br.com.rictodolist.todolist.models.TaskModel;
import br.com.rictodolist.todolist.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/")
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskRequestDTO taskRequestDto, HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");

        TaskModel task = this.taskService.create(taskRequestDto);

        TaskResponseDTO taskResponseDto = new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStartAt(),
                task.getEndAt(),
                task.getPriority(),
                task.getCreatedAt(),
                linkTo(methodOn(TaskController.class).listOne(request, task.getId())).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> listOne(HttpServletRequest request, @PathVariable(value = "id") UUID id) {
        TaskModel task = this.taskService.getOne(id);

        TaskResponseDTO taskResponseDto = new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStartAt(),
                task.getEndAt(),
                task.getPriority(),
                task.getCreatedAt(),
                linkTo(methodOn(TaskController.class).listAll(request)).withRel("Tasks")
        );

        return ResponseEntity.status(HttpStatus.OK).body(taskResponseDto);
    }

    @GetMapping("/")
    public List<TaskResponseDTO> listAll(HttpServletRequest request) {
        List<TaskModel> tasks = this.taskService.getAll();

        List<TaskResponseDTO> taskResponseDtos = new ArrayList<>();

        for (TaskModel task : tasks) {
            taskResponseDtos.add(new TaskResponseDTO(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStartAt(),
                    task.getEndAt(),
                    task.getPriority(),
                    task.getCreatedAt(),
                    linkTo(methodOn(TaskController.class).listOne(request, task.getId())).withSelfRel())
            );
        }

        return taskResponseDtos;
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@Valid @RequestBody TaskUpdateDTO taskUpdateDto, HttpServletRequest request, @PathVariable(value = "id") UUID id) {
        TaskModel taskUpdated = this.taskService.update(taskUpdateDto, id);

        TaskResponseDTO taskResponseDto = new TaskResponseDTO(
                taskUpdated.getId(),
                taskUpdated.getTitle(),
                taskUpdated.getDescription(),
                taskUpdated.getStartAt(),
                taskUpdated.getEndAt(),
                taskUpdated.getPriority(),
                taskUpdated.getCreatedAt(),
                linkTo(methodOn(TaskController.class).listOne(request, taskUpdated.getId())).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.OK).body(taskResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> delete(HttpServletRequest request, @PathVariable(value = "id") UUID id) {
        TaskModel taskDeleted = this.taskService.delete(id);

        TaskResponseDTO taskResponseDto = new TaskResponseDTO(
                taskDeleted.getId(),
                taskDeleted.getTitle(),
                taskDeleted.getDescription(),
                taskDeleted.getStartAt(),
                taskDeleted.getEndAt(),
                taskDeleted.getPriority(),
                taskDeleted.getCreatedAt(),
                linkTo(methodOn(TaskController.class).listOne(request, taskDeleted.getId())).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.OK).body(taskResponseDto);
    }
}
