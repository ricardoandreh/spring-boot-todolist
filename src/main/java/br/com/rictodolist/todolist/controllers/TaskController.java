package br.com.rictodolist.todolist.controllers;

import java.util.List;
import java.util.UUID;

import br.com.rictodolist.todolist.domain.task.TaskModel;
import br.com.rictodolist.todolist.services.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private TaskService taskService;

  @PostMapping("/")
  public ResponseEntity<TaskModel> create(@RequestBody @Valid TaskModel taskModel, HttpServletRequest request) {
    UUID idUser = (UUID) request.getAttribute("idUser");

    TaskModel task = this.taskService.create(taskModel, idUser);

    return ResponseEntity.status(HttpStatus.CREATED).body(task);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskModel> listOne(HttpServletRequest request, @PathVariable UUID id) {
    UUID idUser = (UUID) request.getAttribute("idUser");

    TaskModel task = this.taskService.getOne(id, idUser);

    return ResponseEntity.status(HttpStatus.OK).body(task);
  }

  @GetMapping("/")
  public List<TaskModel> listAll(HttpServletRequest request) {
    UUID idUser = (UUID) request.getAttribute("idUser");

    return this.taskService.getAll(idUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskModel> update(@RequestBody @Valid TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
    UUID idUser = (UUID) request.getAttribute("idUser");

    TaskModel taskUpdated = this.taskService.update(taskModel, id, idUser);

    return ResponseEntity.ok(taskUpdated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<TaskModel> delete(HttpServletRequest request, @PathVariable UUID id) {
    UUID idUser = (UUID) request.getAttribute("idUser");

    TaskModel taskDeleted = this.taskService.delete(id, idUser);

    return ResponseEntity.ok(taskDeleted);
  }
}
