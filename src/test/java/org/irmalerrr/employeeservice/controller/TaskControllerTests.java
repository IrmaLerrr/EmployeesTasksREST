package org.irmalerrr.employeeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.irmalerrr.employeeservice.dto.*;
import org.irmalerrr.employeeservice.enums.TaskStatus;
import org.irmalerrr.employeeservice.exceptions.EmployeeNotFoundException;
import org.irmalerrr.employeeservice.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@MockitoBean(types = JpaMetamodelMappingContext.class)
class TaskControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    private TaskDto createTaskDto(Long id) {
        return new TaskDto(
                id, "title", "description", TaskStatus.OPEN,
                new EmployeeShortDto(1L, "firstName", "lastName"),
                new EmployeeShortDto(1L, "firstName", "lastName"),
                List.of(new EmployeeShortDto(1L, "firstName", "lastName")),
                LocalDateTime.now(), LocalDateTime.now(), LocalDate.now()
        );
    }

    private CreateTaskDto createCreateTaskDto() {
        return new CreateTaskDto(
                "title", "description", TaskStatus.OPEN,
                1L, 1L, List.of(1L), LocalDate.now()
        );
    }

    @Test
    @DisplayName("GET /api/tasks/1 returns 200 OK and a valid JSON")
    void getTask_returnsValidResponseEntity() throws Exception {
        TaskDto mockTask = createTaskDto(1L);
        when(taskService.getTask(1L)).thenReturn(mockTask);

        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.author").exists())
                .andExpect(jsonPath("$.assignee").exists())
                .andExpect(jsonPath("$.viewers").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andExpect(jsonPath("$.deadline").exists());
    }

    @Test
    @DisplayName("GET /api/tasks returns 200 OK and a valid JSON")
    void getTasks_returnsValidResponseEntity() throws Exception {
        TaskDto mockTask1 = createTaskDto(1L);
        TaskDto mockTask2 = createTaskDto(2L);
        when(taskService.getAllTasks()).thenReturn(List.of(mockTask1, mockTask2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @DisplayName("POST /api/tasks returns 201 OK and a valid JSON")
    void createTask_returnsValidResponseEntity() throws Exception {
        CreateTaskDto mockTask1 = createCreateTaskDto();
        TaskDto mockTask2 = createTaskDto(1L);
        when(taskService.createTask(any(CreateTaskDto.class))).thenReturn(mockTask2);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockTask1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/tasks/1 returns 200 OK and a valid JSON")
    void updateTask_returnsValidResponseEntity() throws Exception {
        CreateTaskDto mockTask1 = createCreateTaskDto();
        TaskDto mockTask2 = createTaskDto(1L);
        when(taskService.updateTask(eq(1L), any(CreateTaskDto.class))).thenReturn(mockTask2);

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockTask1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Delete /api/tasks returns 204 OK and a valid JSON")
    void deleteTask_returnsValidResponseEntity() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/tasks with Blank title returns 400")
    void createTask_withBlankTitle_returnsBadRequest() throws Exception {
        CreateTaskDto mockTask = createCreateTaskDto();
        mockTask.setTitle("");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/tasks with null title returns 400")
    void createTask_withNullTitle_returnsBadRequest() throws Exception {
        CreateTaskDto mockTask = createCreateTaskDto();
        mockTask.setTitle(null);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/tasks with null author returns 400")
    void createTask_withNullAuthor_returnsBadRequest() throws Exception {
        CreateTaskDto mockTask = createCreateTaskDto();
        mockTask.setAuthorId(null);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/tasks with not found author returns 404")
    void createTask_withNotFoundAuthor_returnsBadRequest() throws Exception {
        CreateTaskDto mockTask = createCreateTaskDto();
        mockTask.setAuthorId(-1L);
        when(taskService.createTask(any(CreateTaskDto.class))).thenThrow(new EmployeeNotFoundException(-1L));

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockTask)))
                .andExpect(status().isNotFound());
    }
}
