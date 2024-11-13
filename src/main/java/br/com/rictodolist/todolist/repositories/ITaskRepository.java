package br.com.rictodolist.todolist.repositories;

import java.util.List;
import java.util.UUID;

import br.com.rictodolist.todolist.domain.task.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
  List<TaskModel> findByIdUser(UUID idUser);
}
