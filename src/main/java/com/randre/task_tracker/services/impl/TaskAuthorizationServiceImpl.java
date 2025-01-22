package com.randre.task_tracker.services.impl;

import com.randre.task_tracker.repositories.ITaskRepository;
import com.randre.task_tracker.services.interfaces.TaskAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskAuthorizationServiceImpl implements TaskAuthorizationService {

    private final ITaskRepository taskRepository;

    public boolean isTaskOwner(UUID taskId, UUID userId) {

        return this.taskRepository.findById(taskId)
                .map(task -> task.getUser().getId().equals(userId))
                .orElse(false);
    }
}
