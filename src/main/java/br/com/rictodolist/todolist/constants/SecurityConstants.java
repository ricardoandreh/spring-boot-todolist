package br.com.rictodolist.todolist.constants;

public class SecurityConstants {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ROLE_PREFIX = "ROLE_";

    public static final long JWT_ACCESS_EXPIRATION_TIME = 1000 * 60 * 10; // 10 minutes in milliseconds
    public static final long JWT_REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour in milliseconds
}
