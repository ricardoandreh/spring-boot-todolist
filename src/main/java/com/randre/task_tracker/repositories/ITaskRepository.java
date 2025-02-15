package com.randre.task_tracker.repositories;

import com.randre.task_tracker.models.TaskModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {

    Page<TaskModel> findByUserUsername(String username, Pageable pageable);

    @Query
    Page<TaskModel> findByUserUsernameAndTitle(String username, String title, Pageable pageable);
}
