package br.com.rictodolist.todolist.repositories;

import br.com.rictodolist.todolist.models.TaskModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {

    Page<TaskModel> findByUserUsername(String username, Pageable pageable);
}
