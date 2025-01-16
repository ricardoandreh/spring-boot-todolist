package com.randre.task_tracker.mappers;

import com.randre.task_tracker.dtos.task.TaskPaginationDTO;
import com.randre.task_tracker.dtos.task.TaskResponseDTO;
import com.randre.task_tracker.models.TaskModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Component
public class TaskMapper {

    public TaskResponseDTO toDTO(TaskModel model) {
        String selfLink = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(model.getId())
                .toUriString();


        return new TaskResponseDTO(
                model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getStartAt(),
                model.getEndAt(),
                model.getPriority(),
                model.getCreatedAt(),
                Link.of(selfLink, "self")
        );
    }

    public List<TaskResponseDTO> toDTOList(Page<TaskModel> models) {
        return models.getContent().stream()
                .map(this::toDTO)
                .toList();
    }

    public TaskPaginationDTO toPaginationDTO(Page<TaskModel> tasks,
                                             Pageable pageable,
                                             String sortBy,
                                             boolean ascending,
                                             String feedback) {

        List<TaskResponseDTO> taskResponseDtos = this.toDTOList(tasks);

        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        String nextLink = tasks.hasNext()
                ? ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replaceQueryParam("page", currentPage + 1)
                .replaceQueryParam("size", pageSize)
                .replaceQueryParam("sortBy", sortBy)
                .replaceQueryParam("ascending", ascending)
                .toUriString()
                : null;

        String previousLink = tasks.hasPrevious()
                ? ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replaceQueryParam("page", currentPage - 1)
                .replaceQueryParam("size", pageSize)
                .replaceQueryParam("sortBy", sortBy)
                .replaceQueryParam("ascending", ascending)
                .toUriString()
                : null;

        return new TaskPaginationDTO(
                tasks.getTotalElements(),
                tasks.getTotalPages(),
                currentPage,
                pageSize,
                nextLink,
                previousLink,
                taskResponseDtos,
                feedback
        );
    }
}
