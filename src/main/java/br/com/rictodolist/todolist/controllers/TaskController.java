package br.com.rictodolist.todolist.controllers;

import br.com.rictodolist.todolist.dtos.task.TaskPaginationDTO;
import br.com.rictodolist.todolist.dtos.task.TaskRequestDTO;
import br.com.rictodolist.todolist.dtos.task.TaskResponseDTO;
import br.com.rictodolist.todolist.dtos.task.TaskUpdateDTO;
import br.com.rictodolist.todolist.models.TaskModel;
import br.com.rictodolist.todolist.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskRequestDTO taskRequestDto, HttpServletRequest request) {
        TaskModel task = this.taskService.create(taskRequestDto);

        TaskResponseDTO taskResponseDto = new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStartAt(),
                task.getEndAt(),
                task.getPriority(),
                task.getCreatedAt(),
                linkTo(methodOn(TaskController.class).listOne(task.getId())).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> listOne(@PathVariable(value = "id") UUID id) {
        TaskModel task = this.taskService.getOne(id);

        TaskResponseDTO taskResponseDto = new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStartAt(),
                task.getEndAt(),
                task.getPriority(),
                task.getCreatedAt(),
                linkTo(methodOn(TaskController.class).listAll(0, 5, "id", true)).withRel("Tasks")
        );

        return ResponseEntity.status(HttpStatus.OK).body(taskResponseDto);
    }

    @GetMapping
    public ResponseEntity<TaskPaginationDTO> listAll(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size,
                                                     @RequestParam(defaultValue = "id") String sortBy,
                                                     @RequestParam(defaultValue = "true") boolean ascending) {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        List<TaskModel> tasks = this.taskService.getAll(pageable);

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
                    linkTo(methodOn(TaskController.class).listOne(task.getId())).withSelfRel())
            );
        }

        TaskPaginationDTO taskPaginationDTO = new TaskPaginationDTO(
                taskResponseDtos.size(),
                page == 0 ? null : linkTo(methodOn(TaskController.class).listAll(page - 1, 5, "id", true)).withRel("Tasks").getHref(),
                page * size >= taskResponseDtos.size() ? null : linkTo(methodOn(TaskController.class).listAll(page + 1, 5, "id", true)).withRel("Tasks").getHref(),
                taskResponseDtos
        );

        return ResponseEntity.status(HttpStatus.OK).body(taskPaginationDTO);
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
                linkTo(methodOn(TaskController.class).listOne(taskUpdated.getId())).withSelfRel()
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
                linkTo(methodOn(TaskController.class).listOne(taskDeleted.getId())).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.OK).body(taskResponseDto);
    }
}
