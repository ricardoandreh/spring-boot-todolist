package br.com.rictodolist.todolist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageService {

    @Autowired
    private MessageSource messageSource;

    public String getLocalizedMessage(String messageId, Locale locale) {
        return messageSource.getMessage(messageId, null, locale);
    }
}
