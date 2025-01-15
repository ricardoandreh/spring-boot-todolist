package com.randre.task_tracker.controllers;

import com.randre.task_tracker.constants.ErrorMessages;
import com.randre.task_tracker.dtos.task.TaskPaginationDTO;
import com.randre.task_tracker.dtos.task.TaskResponseDTO;
import com.randre.task_tracker.infrastructure.enums.Priority;
import com.randre.task_tracker.repositories.IUserRepository;
import com.randre.task_tracker.services.JwtService;
import com.randre.task_tracker.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @Mock
    private JwtService jwtService;

    @Mock
    private IUserRepository userRepository;

    private TaskResponseDTO baseTaskResponse;
    private TaskResponseDTO baseTaskUpdated;
    private TaskPaginationDTO baseTaskPagination;

    @BeforeEach
    void setUp() throws Exception {
        this.mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "username": "user",
                                "password": "user"
                            }
                        """));

        this.baseTaskResponse = new TaskResponseDTO(
                UUID.randomUUID(),
                "Task 1",
                "Description 1",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.ALTA,
                LocalDateTime.now(),
                Link.of("/tasks/1", "self")
        );
        this.baseTaskUpdated = new TaskResponseDTO(
                UUID.randomUUID(),
                "Task Updated",
                "New description",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.ALTA,
                LocalDateTime.now(),
                Link.of("/tasks/1", "self")
        );
        this.baseTaskPagination = new TaskPaginationDTO(
                3L,
                1,
                1,
                10,
                null,
                null,
                List.of(
                        new TaskResponseDTO(
                                UUID.randomUUID(),
                                "Implementar feature 1",
                                "Descrição da tarefa para implementar a feature 1.",
                                LocalDateTime.now().minusDays(1),
                                LocalDateTime.now().plusDays(2),
                                Priority.ALTA,
                                LocalDateTime.now().minusDays(2),
                                Link.of("/tasks/1", "self")
                        ),
                        new TaskResponseDTO(
                                UUID.randomUUID(),
                                "Implementar feature 2",
                                "Descrição da tarefa para implementar a feature 2.",
                                LocalDateTime.now().minusDays(1),
                                LocalDateTime.now().plusDays(2),
                                Priority.ALTA,
                                LocalDateTime.now().minusDays(2),
                                Link.of("/tasks/2", "self")
                        ),
                        new TaskResponseDTO(
                                UUID.randomUUID(),
                                "Implementar feature 3",
                                "Descrição da tarefa para implementar a feature 3.",
                                LocalDateTime.now().minusDays(1),
                                LocalDateTime.now().plusDays(2),
                                Priority.ALTA,
                                LocalDateTime.now().minusDays(2),
                                Link.of("/tasks/3", "self")
                        )
                )
        );
    }

    @Test
    @DisplayName("Should create a task successfully")
    @WithMockUser(authorities = {"CREATE_TASK"})
    void createTask() throws Exception {
//        when(this.taskService.create(any(TaskRequestDTO.class)))
//                .thenReturn(this.baseTaskResponse);

        this.mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "Task 1",
                                        "description": "Description 1",
                                        "startAt": "2097-12-13T10:00:00",
                                        "endAt": "2097-12-15T11:00:00",
                                        "priority": "ALTA"
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.priority").value("ALTA"));
    }

    @Test
    @DisplayName("Should read all tasks successfully")
    @WithMockUser(authorities = {"READ_ALL_TASKS"})
    void listAllTasks() throws Exception {
//        when(this.taskService.getAll(anyInt(), anyInt(), anyString(), anyBoolean()))
//                .thenReturn(this.baseTaskPagination);

        this.mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.next").value(nullValue()))
                .andExpect(jsonPath("$.previous").value(nullValue()));
    }

    @Test
    @DisplayName("Should read one detail task successfully")
    @WithMockUser(authorities = {"READ_ONE_TASK"})
    void listOneTask() throws Exception {
//        when(this.taskService.getOne(any(UUID.class)))
//                .thenReturn(this.baseTaskResponse);

        this.mockMvc.perform(get("/tasks/" + this.baseTaskResponse.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.priority").value("ALTA"));
    }

    @Test
    @DisplayName("Should update a task successfully")
    @WithMockUser(authorities = {"UPDATE_TASK"})
    void updateTask() throws Exception {
//        when(this.taskService.update(any(TaskUpdateDTO.class), any(UUID.class)))
//                .thenReturn(this.baseTaskUpdated);

        this.mockMvc.perform(patch("/tasks/" + this.baseTaskUpdated.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "Task Updated",
                                        "description": "New description",
                                        "priority": "ALTA"
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Task Updated"))
                .andExpect(jsonPath("$.description").value("New description"))
                .andExpect(jsonPath("$.priority").value("ALTA"));
    }

    @Test
    @DisplayName("Should throw MethodArgumentNotValidException when description is blank validation fails")
    @WithMockUser(authorities = {"UPDATE_TASK"})
    void updateTaskAssertBlank() throws Exception {
//        when(this.taskService.update(any(TaskUpdateDTO.class), any(UUID.class)))
//                .thenReturn(this.baseTaskUpdated);

        this.mockMvc.perform(patch("/tasks/" + this.baseTaskUpdated.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "Task Updated2",
                                        "description": ""
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("must not be blank"))
                .andExpect(jsonPath("$.priority").doesNotExist());
    }

    @Test
    @DisplayName("Should delete a task successfully")
    @WithMockUser(authorities = {"DELETE_TASK"})
    void deleteTask() throws Exception {
//        when(this.taskService.delete(any(UUID.class)))
//                .thenReturn(this.baseTaskResponse);

        this.mockMvc.perform(delete("/tasks/" + this.baseTaskResponse.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.priority").value("ALTA"));
    }

    @Test
    @DisplayName("Should validate date range successfully when everything is OK")
    @WithMockUser(authorities = {"CREATE_TASK"})
    void dateRangeValidation() throws Exception {
        this.mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "Task 1",
                                        "description": "Description 1",
                                        "startAt": "2097-12-17T10:00:00",
                                        "endAt": "2097-12-16T11:00:00",
                                        "priority": 0
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.endAt").value(ErrorMessages.DATE_RANGE));
    }
}
