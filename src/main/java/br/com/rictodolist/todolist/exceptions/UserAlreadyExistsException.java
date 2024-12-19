package br.com.rictodolist.todolist.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(MessageSource ms) {
        super(ms.getMessage("error-messages.user-already-exists", null, LocaleContextHolder.getLocale()));
    }

}
