package com.randre.task_tracker.services;

import com.randre.task_tracker.repositories.ITaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskAuthorizationService {

    private final ITaskRepository taskRepository;

    public boolean isTaskOwner(UUID taskId, UUID userId) {

        return this.taskRepository.findById(taskId)
                .map(task -> task.getUser().getId().equals(userId))
                .orElse(false);
    }
}
