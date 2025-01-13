package com.randre.task_tracker.infrastructure.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RestExceptionMessage {

    private int status;
    private String message;
}
