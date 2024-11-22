package br.com.rictodolist.todolist.config.security;

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
            Permission.DELETE_TASK)
    );

    private List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
