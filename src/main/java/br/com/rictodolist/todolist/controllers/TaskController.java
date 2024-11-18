package br.com.rictodolist.todolist.controllers;

import br.com.rictodolist.todolist.dtos.TaskRequestDTO;
import br.com.rictodolist.todolist.dtos.TaskResponseDTO;
import br.com.rictodolist.todolist.dtos.TaskUpdateDTO;
import br.com.rictodolist.todolist.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/")
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskRequestDTO taskRequestDto, HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");

        TaskResponseDTO task = this.taskService.create(taskRequestDto, idUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> listOne(HttpServletRequest request, @PathVariable(value = "id") UUID id) {
        UUID idUser = (UUID) request.getAttribute("idUser");

        TaskResponseDTO task = this.taskService.getOne(id, idUser);

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskResponseDTO> listAll(HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");

        return this.taskService.getAll(idUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@Valid @RequestBody TaskUpdateDTO taskUpdateDto, HttpServletRequest request, @PathVariable(value = "id") UUID id) {
        UUID idUser = (UUID) request.getAttribute("idUser");

        TaskResponseDTO taskUpdated = this.taskService.update(taskUpdateDto, id, idUser);

        return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> delete(HttpServletRequest request, @PathVariable(value = "id") UUID id) {
        UUID idUser = (UUID) request.getAttribute("idUser");

        TaskResponseDTO taskDeleted = this.taskService.delete(id, idUser);

        return ResponseEntity.status(HttpStatus.OK).body(taskDeleted);
    }
}
