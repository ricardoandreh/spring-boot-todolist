package com.randre.task_tracker.services.interfaces;

import com.randre.task_tracker.dtos.groq.GroqResponseDTO;
import com.randre.task_tracker.models.TaskModel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface GroqService {

    String generateFeedbackByPage(Page<TaskModel> pagedTasks);

    GroqResponseDTO generateFeedbackByIds(List<UUID> ids);

    String performRequest(String tasksParam);
}
