package com.randre.task_tracker.services.interfaces;

import java.util.Locale;

public interface MessageService {

    String getLocalizedMessage(String messageId, Locale locale);
}
