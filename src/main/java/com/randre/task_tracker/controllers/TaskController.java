package com.randre.task_tracker.controllers;

import com.randre.task_tracker.dtos.task.TaskPaginationDTO;
import com.randre.task_tracker.dtos.task.TaskRequestDTO;
import com.randre.task_tracker.dtos.task.TaskResponseDTO;
import com.randre.task_tracker.dtos.task.TaskUpdateDTO;
import com.randre.task_tracker.services.impl.TaskServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO create(@RequestBody @Valid TaskRequestDTO taskRequestDto) {

        return this.taskService.create(taskRequestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDTO listOne(@PathVariable("id") UUID id) {

        return this.taskService.getOne(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskPaginationDTO listAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                     @RequestParam(defaultValue = "true") boolean ascending,
                                     @RequestParam(defaultValue = "false") boolean generateFeedback) {

        return this.taskService.getAll(page, size, sortBy, ascending, generateFeedback);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDTO update(@RequestBody @Valid TaskUpdateDTO taskUpdateDto, @PathVariable("id") UUID id) {

        return this.taskService.update(taskUpdateDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDTO delete(@PathVariable("id") UUID id) {

        return this.taskService.delete(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public TaskPaginationDTO search(@RequestParam(defaultValue = "") String q,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(defaultValue = "createdAt") String sortBy,
                                    @RequestParam(defaultValue = "true") boolean ascending) {

        return this.taskService.search(q, page, size, sortBy, ascending);
    }
}
