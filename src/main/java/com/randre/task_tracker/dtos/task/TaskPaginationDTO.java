package com.randre.task_tracker.dtos.task;

import java.util.List;

public record TaskPaginationDTO(
        long count,
        int totalPages,
        int page,
        int size,
        String next,
        String previous,
        List<TaskResponseDTO> results) {
}
