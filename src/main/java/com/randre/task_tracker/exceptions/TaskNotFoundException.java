package com.randre.task_tracker.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(MessageSource ms) {
        super(ms.getMessage("error-messages.task-not-found", null, LocaleContextHolder.getLocale()));
    }

}
