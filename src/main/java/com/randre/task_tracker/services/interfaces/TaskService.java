package com.randre.task_tracker.services.interfaces;

import com.randre.task_tracker.dtos.task.TaskPaginationDTO;
import com.randre.task_tracker.dtos.task.TaskRequestDTO;
import com.randre.task_tracker.dtos.task.TaskResponseDTO;
import com.randre.task_tracker.dtos.task.TaskUpdateDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

public interface TaskService {

    TaskResponseDTO create(TaskRequestDTO taskRequestDto);

    @PreAuthorize("@taskAuthorizationServiceImpl.isTaskOwner(#id, principal.id)")
    TaskResponseDTO getOne(UUID id);

    TaskPaginationDTO getAll(int page, int size, String sortBy, boolean ascending, boolean generateFeedback);

    TaskPaginationDTO search(String query, int page, int size, String sortBy, boolean ascending);

    @PreAuthorize("@taskAuthorizationServiceImpl.isTaskOwner(#id, principal.id)")
    TaskResponseDTO update(TaskUpdateDTO taskUpdateDto, UUID id);

    @PreAuthorize("@taskAuthorizationServiceImpl.isTaskOwner(#id, principal.id)")
    TaskResponseDTO delete(UUID id);

    Pageable getPageable(int page, int size, String sortBy, boolean ascending);
}
