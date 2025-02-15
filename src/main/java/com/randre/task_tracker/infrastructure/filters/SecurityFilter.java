package com.randre.task_tracker.infrastructure.filters;

import com.randre.task_tracker.infrastructure.enums.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilter {

    private final FilterTaskAuth filterTaskAuth;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionMangConfig ->
                        sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()

                        .requestMatchers(HttpMethod.GET, "/tasks").hasAuthority(Permission.READ_ALL_TASKS.name())
                        .requestMatchers(HttpMethod.GET, "/tasks/{id}").hasAuthority(Permission.READ_ONE_TASK.name())
                        .requestMatchers(HttpMethod.POST, "/tasks").hasAuthority(Permission.CREATE_TASK.name())
                        .requestMatchers(HttpMethod.PATCH, "/tasks/{id}").hasAuthority(Permission.UPDATE_TASK.name())
                        .requestMatchers(HttpMethod.DELETE, "/tasks/{id}").hasAuthority(Permission.DELETE_TASK.name())
                        .requestMatchers(HttpMethod.GET, "/tasks/search\\?**").hasAuthority(Permission.SEARCH_TASKS.name())

                        .requestMatchers(HttpMethod.POST, "/ia/tasks/{ids}").hasAuthority(Permission.GROQ_REQUEST.name())

                        // Dev settings
                        .requestMatchers(HttpMethod.GET, "/docs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll()

                        .anyRequest().permitAll()
                )
                .addFilterBefore(this.filterTaskAuth, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
