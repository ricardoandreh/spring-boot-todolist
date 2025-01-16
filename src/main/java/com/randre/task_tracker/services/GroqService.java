package com.randre.task_tracker.services;

import com.randre.task_tracker.models.TaskModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class GroqService {

    private final ChatClient chatClient;

    @Value("classpath:/prompt-template.st")
    private Resource promptTemplate;

    public GroqService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateFeedback(Page<TaskModel> pagedTasks) {
        String tasks = pagedTasks.getContent().toString();

        return this.chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(this.promptTemplate)
                        .param("tasks", tasks))
                .call()
                .content();
    }
}
