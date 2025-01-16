package com.randre.task_tracker.controllers;

import com.randre.task_tracker.dtos.groq.GroqResponseDTO;
import com.randre.task_tracker.services.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/ia")
public class GroqController {

    @Autowired
    private GroqService groqService;

    @PostMapping("/tasks/{ids}")
    @ResponseStatus(HttpStatus.OK)
    public GroqResponseDTO generateFeedbackByIds(@PathVariable("ids") List<UUID> ids) {

        return this.groqService.generateFeedbackByIds(ids);
    }
}
