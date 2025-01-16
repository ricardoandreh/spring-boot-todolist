package com.randre.task_tracker;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TaskTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApplication.class, args);
    }

    private final OpenAiChatModel chatModel;

    @Autowired
    public TaskTrackerApplication(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            System.out.println(this.chatModel.call("Generate the names of 5 famous pirates."));
        };
    }
}
