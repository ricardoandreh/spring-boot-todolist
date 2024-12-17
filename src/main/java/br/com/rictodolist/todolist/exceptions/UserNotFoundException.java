package br.com.rictodolist.todolist.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("error-messages.user-not-found", null, LocaleContextHolder.getLocale()));
    }

    public UserNotFoundException(MessageSource messageSource, String username) {
        super(String.format("%s: %s",
                messageSource.getMessage("error-messages.user-not-found", null, LocaleContextHolder.getLocale()), username));
    }
}
