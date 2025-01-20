package com.randre.task_tracker.services;

import com.randre.task_tracker.dtos.task.TaskPaginationDTO;
import com.randre.task_tracker.dtos.task.TaskRequestDTO;
import com.randre.task_tracker.dtos.task.TaskResponseDTO;
import com.randre.task_tracker.dtos.task.TaskUpdateDTO;
import com.randre.task_tracker.exceptions.TaskNotFoundException;
import com.randre.task_tracker.exceptions.UserNotFoundException;
import com.randre.task_tracker.mappers.TaskMapper;
import com.randre.task_tracker.models.TaskModel;
import com.randre.task_tracker.models.UserModel;
import com.randre.task_tracker.repositories.ITaskRepository;
import com.randre.task_tracker.repositories.IUserRepository;
import com.randre.task_tracker.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private GroqService groqService;

    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private MessageSource messageSource;

    public TaskResponseDTO create(TaskRequestDTO taskRequestDto) {
        TaskModel taskModel = new TaskModel();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserDetails user = this.findUserByUsername(username);

        BeanUtils.copyProperties(taskRequestDto, taskModel);
        taskModel.setUser((UserModel) user);

        TaskModel taskCreated = this.taskRepository.save(taskModel);

        return this.taskMapper.toDTO(taskCreated);
    }


    @PreAuthorize("@taskAuthorizationService.isTaskOwner(#id, principal.id)")
    public TaskResponseDTO getOne(UUID id) {
        TaskModel task = this.findTaskById(id);

        return this.taskMapper.toDTO(task);
    }

    public TaskPaginationDTO getAll(int page, int size, String sortBy, boolean ascending, boolean generateFeedback) {
        Pageable pageable = this.getPageable(page, size, sortBy, ascending);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<TaskModel> tasks = this.taskRepository.findByUserUsername(username, pageable);

        String feedback = generateFeedback ? this.groqService.generateFeedbackByPage(tasks) : null;

        return this.taskMapper.toPaginationDTO(tasks, pageable, sortBy, ascending, feedback);
    }

    public TaskPaginationDTO search(String query, int page, int size, String sortBy, boolean ascending) {
        Pageable pageable = this.getPageable(page, size, sortBy, ascending);

        Page<TaskModel> tasks = this.taskRepository.findByTitle(query, pageable);

        return this.taskMapper.toPaginationDTO(tasks, pageable, sortBy, ascending, null);
    }

    @PreAuthorize("@taskAuthorizationService.isTaskOwner(#id, principal.id)")
    public TaskResponseDTO update(TaskUpdateDTO taskUpdateDto, UUID id) {
        TaskModel task = this.findTaskById(id);

        Utils.copyNonNullProperties(taskUpdateDto, task);

        TaskModel taskCreated = this.taskRepository.save(task);

        return this.taskMapper.toDTO(taskCreated);
    }

    @PreAuthorize("@taskAuthorizationService.isTaskOwner(#id, principal.id)")
    public TaskResponseDTO delete(UUID id) {
        TaskModel task = this.findTaskById(id);

        this.taskRepository.delete(task);

        return this.taskMapper.toDTO(task);
    }

    public TaskModel findTaskById(UUID id) {

        return this.taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(this.messageSource));
    }

    public UserDetails findUserByUsername(String username) {

        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(this.messageSource));
    }

    public Pageable getPageable(int page, int size, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }
}
