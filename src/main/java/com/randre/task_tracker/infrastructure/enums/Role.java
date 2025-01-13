package com.randre.task_tracker.infrastructure.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Role {

    USER(Arrays.asList(
            Permission.READ_ALL_TASKS,
            Permission.READ_ONE_TASK,
            Permission.CREATE_TASK,
            Permission.UPDATE_TASK,
            Permission.DELETE_TASK,
            Permission.SEARCH_TASKS)
    );

    private final List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
