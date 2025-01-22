package com.randre.task_tracker.services.impl;

import com.randre.task_tracker.services.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;

    public String getLocalizedMessage(String messageId, Locale locale) {
        return messageSource.getMessage(messageId, null, locale);
    }
}
