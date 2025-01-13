package com.randre.task_tracker.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(MessageSource ms) {
        super(ms.getMessage("error-messages.user-not-found", null, LocaleContextHolder.getLocale()));
    }

    public UserNotFoundException(MessageSource ms, String username) {
        super(String.format("%s: %s",
                ms.getMessage("error-messages.user-not-found", null, LocaleContextHolder.getLocale()), username));
    }
}
