package org.irmalerrr.employeeservice.service;

import org.irmalerrr.employeeservice.dto.CreateTaskDto;
import org.irmalerrr.employeeservice.dto.EmployeeShortDto;
import org.irmalerrr.employeeservice.dto.TaskDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.entity.Task;
import org.irmalerrr.employeeservice.enums.TaskStatus;
import org.irmalerrr.employeeservice.exceptions.EmployeeNotFoundException;
import org.irmalerrr.employeeservice.exceptions.TaskNotFoundException;
import org.irmalerrr.employeeservice.mapper.TaskMapper;
import org.irmalerrr.employeeservice.repository.EmployeeRepository;
import org.irmalerrr.employeeservice.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("getTask should return TaskDto when task exists")
    void getTask_ShouldReturnTaskDto() {
        Long taskId = 1L;
        Task expectedEntity = createTask(taskId);
        TaskDto expectedDto = createTaskDto(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(expectedEntity));
        when(taskMapper.toDto(expectedEntity)).thenReturn(expectedDto);

        TaskDto actualDto = taskService.getTask(taskId);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(taskRepository).findById(taskId);
        verify(taskMapper).toDto(expectedEntity);
    }

    @Test
    @DisplayName("getTask should throw TaskNotFoundException when task not found")
    void getTask_ShouldThrowTaskNotFoundException() {
        Long taskId = 0L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask(taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with id: " + taskId);

        verify(taskRepository).findById(taskId);
        verify(taskMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("getAllTasks should return list of TaskDto")
    void getAllTasks_ShouldReturnListOfTaskDto() {
        List<Task> tasks = List.of(createTask(1L), createTask(2L));
        List<TaskDto> expectedDtos = List.of(createTaskDto(1L), createTaskDto(2L));

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toDtoList(tasks)).thenReturn(expectedDtos);

        List<TaskDto> actualDtos = taskService.getAllTasks();

        assertThat(actualDtos).hasSize(2)
                .isEqualTo(expectedDtos);
        verify(taskRepository).findAll();
        verify(taskMapper).toDtoList(tasks);
    }

    @Test
    @DisplayName("getAllTasks should return empty list when no tasks")
    void getAllTasks_ShouldReturnEmptyList() {
        when(taskRepository.findAll()).thenReturn(List.of());
        when(taskMapper.toDtoList(List.of())).thenReturn(List.of());

        List<TaskDto> actualDtos = taskService.getAllTasks();

        assertThat(actualDtos).isEmpty();
        verify(taskRepository).findAll();
        verify(taskMapper).toDtoList(List.of());
    }

    @Test
    @DisplayName("createTask should create task with all relations when all employees exist")
    void createTask_ShouldCreateTaskWithAllRelations() {
        CreateTaskDto dto = createCreateTaskDto();
        Employee author = createEmployee(1L);
        Employee assignee = createEmployee(2L);
        Employee viewer1 = createEmployee(3L);
        Employee viewer2 = createEmployee(4L);
        List<Employee> viewers = List.of(viewer1, viewer2);
        List<Employee> allEmployees = List.of(author, assignee, viewer1, viewer2);
        Task taskToSave = createTask(null);
        Task savedTask = createTask(1L);
        TaskDto expectedDto = createTaskDto(1L);

        when(employeeRepository.findAllById(List.of(1L, 2L, 3L, 4L))).thenReturn(allEmployees);
        when(taskMapper.toEntity(dto, author, assignee, viewers)).thenReturn(taskToSave);
        when(taskRepository.save(taskToSave)).thenReturn(savedTask);
        when(taskMapper.toDto(savedTask)).thenReturn(expectedDto);

        TaskDto actualDto = taskService.createTask(dto);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(employeeRepository).findAllById(List.of(1L, 2L, 3L, 4L));
        verify(taskMapper).toEntity(dto, author, assignee, viewers);
        verify(taskRepository).save(taskToSave);
        verify(taskMapper).toDto(savedTask);
    }

    @Test
    @DisplayName("createTask should create task without assignee when assigneeId is null")
    void createTask_ShouldCreateTaskWithoutAssignee() {
        CreateTaskDto dto = createCreateTaskDto().setAssigneeId(null);
        Employee author = createEmployee(1L);
        Employee viewer1 = createEmployee(3L);
        Employee viewer2 = createEmployee(4L);
        List<Employee> viewers = List.of(viewer1, viewer2);
        List<Employee> allEmployees = List.of(author, viewer1, viewer2);
        Task taskToSave = createTask(null);
        Task savedTask = createTask(1L);
        TaskDto expectedDto = createTaskDto(1L).setAssignee(null);

        when(employeeRepository.findAllById(List.of(1L, 3L, 4L))).thenReturn(allEmployees);
        when(taskMapper.toEntity(dto, author, null, viewers)).thenReturn(taskToSave);
        when(taskRepository.save(taskToSave)).thenReturn(savedTask);
        when(taskMapper.toDto(savedTask)).thenReturn(expectedDto);

        TaskDto actualDto = taskService.createTask(dto);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(employeeRepository).findAllById(List.of(1L, 3L, 4L));
        verify(taskMapper).toEntity(dto, author, null, viewers);
        verify(taskRepository).save(taskToSave);
    }

    @Test
    @DisplayName("createTask should create task without viewers when viewersIds is null")
    void createTask_ShouldCreateTaskWithoutViewers() {
        CreateTaskDto dto = createCreateTaskDto().setViewersIds(null);
        Employee author = createEmployee(1L);
        Employee assignee = createEmployee(2L);
        List<Employee> allEmployees = List.of(author, assignee);
        Task taskToSave = createTask(null);
        Task savedTask = createTask(1L);
        TaskDto expectedDto = createTaskDto(1L).setViewers(null);

        when(employeeRepository.findAllById(List.of(1L, 2L))).thenReturn(allEmployees);
        when(taskMapper.toEntity(dto, author, assignee, new ArrayList<>())).thenReturn(taskToSave);
        when(taskRepository.save(taskToSave)).thenReturn(savedTask);
        when(taskMapper.toDto(savedTask)).thenReturn(expectedDto);

        TaskDto actualDto = taskService.createTask(dto);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(employeeRepository).findAllById(List.of(1L, 2L));
        verify(taskMapper).toEntity(dto, author, assignee, new ArrayList<>());
    }

    @Test
    @DisplayName("createTask should create task with empty viewers list when viewersIds is empty")
    void createTask_ShouldCreateTaskWithEmptyViewersList() {
        CreateTaskDto dto = createCreateTaskDto().setViewersIds(List.of());
        Employee author = createEmployee(1L);
        Employee assignee = createEmployee(2L);
        List<Employee> allEmployees = List.of(author, assignee);
        Task taskToSave = createTask(null);
        Task savedTask = createTask(1L);
        TaskDto expectedDto = createTaskDto(1L).setViewers(List.of());

        when(employeeRepository.findAllById(List.of(1L, 2L))).thenReturn(allEmployees);
        when(taskMapper.toEntity(dto, author, assignee, new ArrayList<>())).thenReturn(taskToSave);
        when(taskRepository.save(taskToSave)).thenReturn(savedTask);
        when(taskMapper.toDto(savedTask)).thenReturn(expectedDto);

        TaskDto actualDto = taskService.createTask(dto);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(employeeRepository).findAllById(List.of(1L, 2L));
    }

    @Test
    @DisplayName("createTask should throw EmployeeNotFoundException when author not found")
    void createTask_ShouldThrowEmployeeNotFoundExceptionWhenAuthorNotFound() {
        CreateTaskDto dto = createCreateTaskDto();

        Employee assignee = createEmployee(2L);
        Employee viewer1 = createEmployee(3L);
        Employee viewer2 = createEmployee(4L);
        List<Employee> allEmployees = List.of(assignee, viewer1, viewer2);

        when(employeeRepository.findAllById(List.of(1L, 2L, 3L, 4L))).thenReturn(allEmployees);

        assertThatThrownBy(() -> taskService.createTask(dto))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findAllById(List.of(1L, 2L, 3L, 4L));
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("createTask should throw EmployeeNotFoundException when assignee not found")
    void createTask_ShouldThrowEmployeeNotFoundExceptionWhenAssigneeNotFound() {
        CreateTaskDto dto = createCreateTaskDto();

        Employee author = createEmployee(1L);
        Employee viewer1 = createEmployee(3L);
        Employee viewer2 = createEmployee(4L);
        List<Employee> allEmployees = List.of(author, viewer1, viewer2);

        when(employeeRepository.findAllById(List.of(1L, 2L, 3L, 4L))).thenReturn(allEmployees);

        assertThatThrownBy(() -> taskService.createTask(dto))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with id: 2");

        verify(employeeRepository).findAllById(List.of(1L, 2L, 3L, 4L));
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("createTask should throw EmployeeNotFoundException when viewer not found")
    void createTask_ShouldThrowEmployeeNotFoundExceptionWhenViewerNotFound() {
        CreateTaskDto dto = createCreateTaskDto();

        Employee author = createEmployee(1L);
        Employee assignee = createEmployee(2L);
        Employee viewer2 = createEmployee(4L);
        List<Employee> allEmployees = List.of(author, assignee, viewer2);

        when(employeeRepository.findAllById(List.of(1L, 2L, 3L, 4L))).thenReturn(allEmployees);

        assertThatThrownBy(() -> taskService.createTask(dto))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with id: 3");

        verify(employeeRepository).findAllById(List.of(1L, 2L, 3L, 4L));
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("createTask should throw EmployeeNotFoundException when employees not found")
    void createTask_ShouldThrowEmployeeNotFoundExceptionWhenEmployeesNotFound() {
        CreateTaskDto dto = createCreateTaskDto();

        Employee author = createEmployee(1L);
        Employee viewer2 = createEmployee(4L);
        List<Employee> allEmployees = List.of(author, viewer2);

        when(employeeRepository.findAllById(List.of(1L, 2L, 3L, 4L))).thenReturn(allEmployees);

        assertThatThrownBy(() -> taskService.createTask(dto))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employees not found for IDs: [2, 3]");

        verify(employeeRepository).findAllById(List.of(1L, 2L, 3L, 4L));
        verify(taskRepository, never()).save(any());
    }


    @Test
    @DisplayName("updateTask should update task when task and all employees exist")
    void updateTask_ShouldUpdateTask() {
        Long taskId = 1L;
        CreateTaskDto dto = createCreateTaskDto();
        Task existingTask = createTask(taskId);
        existingTask.setViewers(new ArrayList<>(List.of(createEmployee(3L), createEmployee(4L))));

        Employee author = createEmployee(1L);
        Employee assignee = createEmployee(2L);
        List<Employee> allEmployees = List.of(
                author,
                assignee,
                createEmployee(3L),
                createEmployee(4L)
        );
        Task updatedTask = createTask(taskId);
        TaskDto expectedDto = createTaskDto(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(employeeRepository.findAllById(any())).thenReturn(allEmployees);
        when(taskMapper.updateEntity(existingTask, dto, author, assignee)).thenReturn(updatedTask);
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(expectedDto);

        TaskDto actualDto = taskService.updateTask(taskId, dto);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(taskRepository).findById(taskId);
        verify(employeeRepository).findAllById(any());
        verify(taskMapper).updateEntity(existingTask, dto, author, assignee);
        verify(taskRepository).save(updatedTask);
        verify(taskMapper).toDto(updatedTask);
    }

    @Test
    @DisplayName("updateTask should update viewers - remove old and add new")
    void updateTask_ShouldUpdateViewersCorrectly() {
        Long taskId = 1L;
        CreateTaskDto dto = createCreateTaskDto().setViewersIds(List.of(4L, 5L));
        Task existingTask = createTask(taskId);
        existingTask.setViewers(new ArrayList<>(List.of(createEmployee(3L), createEmployee(4L))));

        Employee author = createEmployee(1L);
        Employee assignee = createEmployee(2L);
        List<Employee> allEmployees = List.of(
                author,
                assignee,
                createEmployee(4L),
                createEmployee(5L)
        );
        Task updatedTask = createTask(taskId);
        TaskDto expectedDto = createTaskDto(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(employeeRepository.findAllById(any())).thenReturn(allEmployees);
        when(taskMapper.updateEntity(existingTask, dto, author, assignee)).thenReturn(updatedTask);
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(expectedDto);

        TaskDto actualDto = taskService.updateTask(taskId, dto);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(taskRepository).findById(taskId);
        verify(taskMapper).updateEntity(existingTask, dto, author, assignee);
        verify(taskRepository).save(updatedTask);
    }

    @Test
    @DisplayName("updateTask should throw TaskNotFoundException when task not found")
    void updateTask_ShouldThrowTaskNotFoundException() {
        Long taskId = 0L;
        CreateTaskDto dto = createCreateTaskDto();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(taskId, dto))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with id: " + taskId);

        verify(taskRepository).findById(taskId);
        verify(employeeRepository, never()).findById(any());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateTask should throw EmployeeNotFoundException when any employee not found during update")
    void updateTask_ShouldThrowEmployeeNotFoundExceptionWhenEmployeeNotFound() {
        Long taskId = 1L;
        CreateTaskDto dto = createCreateTaskDto();
        Task existingTask = createTask(taskId);
        List<Long> expectedIds = List.of(1L, 2L, 3L, 4L);
        List<Employee> foundEmployees = List.of(createEmployee(1L), createEmployee(2L), createEmployee(4L));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        when(employeeRepository.findAllById(expectedIds)).thenReturn(foundEmployees);

        assertThatThrownBy(() -> taskService.updateTask(taskId, dto))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with id: 3");

        verify(taskRepository).findById(taskId);
        verify(employeeRepository).findAllById(expectedIds);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateTask should throw EmployeeNotFoundException when some employees not found during update")
    void updateTask_ShouldThrowEmployeeNotFoundExceptionWhenEmployeesNotFound() {
        Long taskId = 1L;
        CreateTaskDto dto = createCreateTaskDto();
        Task existingTask = createTask(taskId);
        List<Long> expectedIds = List.of(1L, 2L, 3L, 4L);
        List<Employee> foundEmployees = List.of(createEmployee(1L), createEmployee(4L));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        when(employeeRepository.findAllById(expectedIds)).thenReturn(foundEmployees);

        assertThatThrownBy(() -> taskService.updateTask(taskId, dto))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employees not found for IDs: [2, 3]");

        verify(taskRepository).findById(taskId);
        verify(employeeRepository).findAllById(expectedIds);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteTask should delete task when task exists")
    void deleteTask_ShouldDeleteTask() {
        Long taskId = 1L;
        Task task = createTask(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        taskService.deleteTask(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("deleteTask should throw TaskNotFoundException when task not found")
    void deleteTask_ShouldThrowTaskNotFoundException() {
        Long taskId = 0L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with id: " + taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).delete(any());
    }

    private Employee createEmployee(Long id) {
        return Employee.builder()
                .id(id)
                .firstName("firstName")
                .lastName("lastName")
                .build();
    }
    private EmployeeShortDto createEmployeeShortDto(Long id) {
        return EmployeeShortDto.builder()
                .id(id)
                .firstName("firstName")
                .lastName("lastName")
                .build();
    }

    private Task createTask(Long id) {
        return Task.builder()
                .id(id)
                .title("title")
                .description("description")
                .status(TaskStatus.OPEN)
                .deadline(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(createEmployee(1L))
                .assignee(createEmployee(2L))
                .viewers(new ArrayList<>(List.of(createEmployee(3L), createEmployee(4L))))
                .build();
    }

    private CreateTaskDto createCreateTaskDto() {
        return CreateTaskDto.builder()
                .title("title")
                .description("description")
                .status(TaskStatus.OPEN)
                .authorId(1L)
                .assigneeId(2L)
                .viewersIds(List.of(3L, 4L))
                .deadline(LocalDate.now())
                .build();
    }

    private TaskDto createTaskDto(Long id) {
        return TaskDto.builder()
                .id(id)
                .title("title")
                .description("description")
                .status(TaskStatus.OPEN)
                .author(createEmployeeShortDto(1L))
                .assignee(createEmployeeShortDto(2L))
                .viewers(List.of(createEmployeeShortDto(3L), createEmployeeShortDto(4L)))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deadline(LocalDate.now())
                .build();
    }
}