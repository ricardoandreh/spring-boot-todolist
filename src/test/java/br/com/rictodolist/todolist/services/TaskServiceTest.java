package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.dtos.task.TaskPaginationDTO;
import br.com.rictodolist.todolist.dtos.task.TaskRequestDTO;
import br.com.rictodolist.todolist.dtos.task.TaskResponseDTO;
import br.com.rictodolist.todolist.dtos.task.TaskUpdateDTO;
import br.com.rictodolist.todolist.exceptions.AccessDeniedException;
import br.com.rictodolist.todolist.infrastructure.enums.Priority;
import br.com.rictodolist.todolist.infrastructure.enums.Role;
import br.com.rictodolist.todolist.mappers.TaskMapper;
import br.com.rictodolist.todolist.models.TaskModel;
import br.com.rictodolist.todolist.models.UserModel;
import br.com.rictodolist.todolist.repositories.ITaskRepository;
import br.com.rictodolist.todolist.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Spy
    private TaskMapper taskMapper;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ITaskRepository taskRepository;

    private UUID taskId;
    private UserModel baseUser;
    private TaskModel baseTask;
    private List<TaskModel> tasksList;
    private TaskUpdateDTO taskUpdateDto;
    private TaskRequestDTO taskRequestDto;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        this.baseUser = new UserModel(
                UUID.randomUUID(),
                "user",
                null,
                "rawPassword",
                LocalDateTime.now(),
                Role.USER,
                null
        );

        this.baseTask = new TaskModel(
                UUID.randomUUID(),
                "title",
                "description",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Priority.BAIXA,
                LocalDateTime.now(),
                this.baseUser
        );

        this.taskId = this.baseTask.getId();

        this.taskRequestDto = new TaskRequestDTO(
                this.baseTask.getTitle(),
                this.baseTask.getDescription(),
                this.baseTask.getStartAt(),
                this.baseTask.getEndAt(),
                this.baseTask.getPriority()
        );

        this.taskUpdateDto = new TaskUpdateDTO(
                "new title",
                "new description",
                null,
                null,
                null
        );

        this.tasksList = List.of(
                new TaskModel(
                        UUID.randomUUID(),
                        "Task 1",
                        "Description 1",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1),
                        Priority.ALTA,
                        LocalDateTime.now(),
                        this.baseUser
                ),
                new TaskModel(
                        UUID.randomUUID(),
                        "Task 2",
                        "Description 2",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2),
                        Priority.BAIXA,
                        LocalDateTime.now(),
                        this.baseUser
                ),
                new TaskModel(
                        UUID.randomUUID(),
                        "Task 3",
                        "Description 3",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1),
                        Priority.MEDIA,
                        LocalDateTime.now(),
                        this.baseUser
                )
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(this.baseUser.getUsername(), null)
        );
    }

    @Test
    @DisplayName("Should create a task successfully when everything is OK")
    void createTaskSuccessfully() {
        when(this.userRepository.findByUsername(this.baseUser.getUsername()))
                .thenReturn(Optional.of(this.baseUser));
        when(this.taskRepository.save(any(TaskModel.class)))
                .thenReturn(this.baseTask);

        TaskResponseDTO taskResponseDTO = this.taskService.create(this.taskRequestDto);

        assertNotNull(taskResponseDTO);
        assertEquals("title", taskResponseDTO.title());

        verify(this.userRepository, times(1))
                .findByUsername(this.baseUser.getUsername());
        verify(this.taskRepository, times(1))
                .save(any(TaskModel.class));
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when creation fails")
    void createTaskUsernameNotFoundException() {
        when(this.userRepository.findByUsername("user"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                this.taskService.create(this.taskRequestDto));

        verify(this.userRepository, times(1))
                .findByUsername(this.baseUser.getUsername());
        verify(this.taskRepository, never())
                .save(any(TaskModel.class));
    }

    @Test
    @DisplayName("Should fetch one task successfully when everything is OK")
    void getOneTaskSuccessfully() {
        when(this.taskRepository.findById(this.taskId))
                .thenReturn(Optional.of(this.baseTask));

        TaskResponseDTO taskResponseDto = this.taskService.getOne(this.taskId);

        assertNotNull(taskResponseDto);

        verify(this.taskRepository, times(1))
                .findById(this.taskId);
        verify(this.taskMapper, times(1))
                .toDTO(this.baseTask);
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when fetching one fails")
    void getOneTaskAccessDeniedException() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("anotheruser", null)
        );

        when(this.taskRepository.findById(this.taskId))
                .thenReturn(Optional.of(this.baseTask));

        assertThrows(AccessDeniedException.class, () -> this.taskService.getOne(this.taskId));

        verify(this.taskRepository, times(1))
                .findById(this.taskId);
        verify(this.taskMapper, never())
                .toDTO(this.baseTask);
    }

    @Test
    @DisplayName("Should return paginated tasks [PAGE 0] for the authenticated user")
    void getAllTasksSuccessfullyPage2() {
        int page = 0, size = 3;
        String sortBy = "title";
        boolean ascending = true;

        Pageable pageable = this.taskService.getPageable(page, size, sortBy, ascending);

        Page<TaskModel> tasksPage = new PageImpl<>(this.tasksList, pageable, this.tasksList.size());

        when(this.taskRepository.findByUserUsername(this.baseUser.getUsername(), pageable))
                .thenReturn(tasksPage);

        TaskPaginationDTO taskPaginationDto = this.taskService.getAll(page, size, sortBy, ascending);

        assertNotNull(taskPaginationDto);
        assertEquals("Task 1", taskPaginationDto.results().get(0).title());
        assertEquals(3, taskPaginationDto.count());
        assertEquals(3, taskPaginationDto.results().size());
        assertEquals(1, taskPaginationDto.totalPages());

        verify(this.taskRepository, times(1))
                .findByUserUsername(this.baseUser.getUsername(), pageable);
        verify(this.taskMapper, times(1))
                .toPaginationDTO(tasksPage, pageable, sortBy, ascending);
    }

    @Test
    @DisplayName("Should return paginated tasks [PAGE 1] with descending order for the authenticated user")
    void getAllTasksSuccessfullyPage1() {
        int page = 1, size = 2;
        String sortBy = "title";
        boolean ascending = false;

        Pageable pageable = this.taskService.getPageable(page, size, sortBy, ascending);

        List<TaskModel> sortedTasks = this.tasksList.stream()
                .sorted(Comparator.comparing(TaskModel::getTitle).reversed())
                .toList();

        int start = Math.min((int) pageable.getOffset(), sortedTasks.size());
        int end = Math.min((start + pageable.getPageSize()), sortedTasks.size());
        List<TaskModel> paginatedTasks = sortedTasks.subList(start, end);

        Page<TaskModel> tasksPage = new PageImpl<>(paginatedTasks, pageable, sortedTasks.size());

        when(this.taskRepository.findByUserUsername(this.baseUser.getUsername(), pageable))
                .thenReturn(tasksPage);

        TaskPaginationDTO taskPaginationDto = this.taskService.getAll(page, size, sortBy, ascending);

        assertNotNull(taskPaginationDto);
        assertEquals("Task 1", taskPaginationDto.results().get(0).title());
        assertEquals(3, taskPaginationDto.count());
        assertEquals(1, taskPaginationDto.results().size());
        assertEquals(2, taskPaginationDto.totalPages());

        verify(this.taskRepository, times(1))
                .findByUserUsername(this.baseUser.getUsername(), pageable);
        verify(this.taskMapper, times(1))
                .toPaginationDTO(tasksPage, pageable, sortBy, ascending);
    }

    @Test
    @DisplayName("Should update a task successfully when everything is OK")
    void updateTaskSuccessfully() {
        when(this.taskRepository.findById(this.taskId))
                .thenReturn(Optional.of(this.baseTask));
        when(this.taskRepository.save(any(TaskModel.class)))
                .thenReturn(this.baseTask);

        TaskResponseDTO taskResponseDTO = this.taskService.update(this.taskUpdateDto, this.taskId);

        assertNotNull(taskResponseDTO);

        verify(this.taskRepository, times(1))
                .findById(this.taskId);
        verify(this.taskRepository, times(1))
                .save(any(TaskModel.class));
        verify(this.taskMapper, times(1))
                .toDTO(any(TaskModel.class));
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when updating fails")
    void updateTaskAccessDeniedException() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("anotheruser", null)
        );

        when(this.taskRepository.findById(this.taskId))
                .thenReturn(Optional.of(this.baseTask));

        assertThrows(AccessDeniedException.class, () ->
                this.taskService.update(this.taskUpdateDto, this.taskId));

        verify(this.taskRepository, times(1))
                .findById(this.taskId);
        verify(this.taskMapper, never())
                .toDTO(this.baseTask);
    }

    @Test
    @DisplayName("Should delete a task successfully when everything is OK")
    void deleteTaskSuccessfully() {
        when(this.taskRepository.findById(this.taskId))
                .thenReturn(Optional.of(this.baseTask));

        this.taskService.delete(this.taskId);

        verify(this.taskRepository, times(1))
                .findById(this.taskId);
        verify(this.taskRepository, times(1))
                .delete(any(TaskModel.class));
        verify(this.taskMapper, times(1))
                .toDTO(any(TaskModel.class));
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when deleting fails")
    void deleteTaskAccessDeniedException() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("anotheruser", null)
        );

        when(this.taskRepository.findById(this.taskId))
                .thenReturn(Optional.of(this.baseTask));

        assertThrows(AccessDeniedException.class, () ->
                this.taskService.delete(this.taskId));

        verify(this.taskRepository, times(1))
                .findById(this.taskId);
        verify(this.taskRepository, never())
                .delete(any(TaskModel.class));
        verify(this.taskMapper, never())
                .toDTO(any(TaskModel.class));
    }
}
