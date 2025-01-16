package com.randre.task_tracker.services;

import com.randre.task_tracker.dtos.groq.GroqResponseDTO;
import com.randre.task_tracker.models.TaskModel;
import com.randre.task_tracker.repositories.ITaskRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroqService {

    @Value("classpath:/prompt-template.st")
    private Resource promptTemplate;

    @Autowired
    private ITaskRepository taskRepository;

    private final ChatClient chatClient;

    public GroqService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateFeedbackByPage(Page<TaskModel> pagedTasks) {
        String tasks = pagedTasks.getContent().toString();

        return this.performRequest(tasks);
    }

    public GroqResponseDTO generateFeedbackByIds(List<UUID> ids) {
        List<TaskModel> tasksList = this.taskRepository.findAllById(ids);

        String tasks = tasksList.toString();

        String content = this.performRequest(tasks);

        return new GroqResponseDTO(content);
    }

    public String performRequest(String tasksParam) {

        return this.chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(this.promptTemplate)
                        .param("tasks", tasksParam))
                .call()
                .content();
    }
}
