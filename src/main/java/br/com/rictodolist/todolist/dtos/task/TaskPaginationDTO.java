package br.com.rictodolist.todolist.dtos.task;

import org.springframework.hateoas.Link;

import java.util.List;

public record TaskPaginationDTO(
        int count,
        String next,
        String previous,
        List<TaskResponseDTO> results) {
}
