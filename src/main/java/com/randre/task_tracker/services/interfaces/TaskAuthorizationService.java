package com.randre.task_tracker.services.interfaces;

import java.util.UUID;

public interface TaskAuthorizationService {

    boolean isTaskOwner(UUID taskId, UUID userId);
}
