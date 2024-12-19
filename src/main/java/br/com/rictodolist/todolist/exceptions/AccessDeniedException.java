package br.com.rictodolist.todolist.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(MessageSource ms) {
        super(ms.getMessage("error-messages.access-denied", null, LocaleContextHolder.getLocale()));
    }


    public AccessDeniedException(String message) {
        super(message);
    }
}
