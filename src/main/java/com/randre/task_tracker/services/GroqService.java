package com.randre.task_tracker.services;

import com.randre.task_tracker.dtos.groq.GroqResponseDTO;
import com.randre.task_tracker.models.TaskModel;
import com.randre.task_tracker.repositories.ITaskRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@PreAuthorize("hasAuthority('GROQ_REQUEST')")
public class GroqService {

    @Value("classpath:/system-prompt-template.st")
    private Resource systemPromptTemplate;

    @Value("classpath:/user-prompt-template.st")
    private Resource userPromptTemplate;

    private final ChatClient chatClient;

    private final ITaskRepository taskRepository;

    public GroqService(ChatClient.Builder chatClientBuilder, ITaskRepository taskRepository) {
        this.chatClient = chatClientBuilder.build();
        this.taskRepository = taskRepository;
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
                .system(systemSpec -> systemSpec
                        .text(this.systemPromptTemplate))
                .user(userSpec -> userSpec
                        .text(this.userPromptTemplate)
                        .param("tasks", tasksParam))
                .call()
                .content();
    }
}
