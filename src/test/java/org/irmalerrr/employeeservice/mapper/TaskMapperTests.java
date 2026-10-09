package org.irmalerrr.employeeservice.mapper;

import org.irmalerrr.employeeservice.dto.CreateTaskDto;
import org.irmalerrr.employeeservice.dto.TaskDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.entity.Task;
import org.irmalerrr.employeeservice.enums.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {EmployeeMapperImpl.class, TaskMapperImpl.class})
class TaskMapperTests {
    @Autowired
    private TaskMapper taskMapper;

    @Test
    @DisplayName("toDto should map Task to TaskDto")
    void toDto_ShouldMapTaskToTaskDto() {
        Task task = createTask(1L);
        TaskDto taskDto = taskMapper.toDto(task);

        assertThat(taskDto).isNotNull();
        assertThat(taskDto.getId()).isEqualTo(task.getId());
        assertThat(taskDto.getTitle()).isEqualTo(task.getTitle());
        assertThat(taskDto.getDescription()).isEqualTo(task.getDescription());
        assertThat(taskDto.getStatus()).isEqualTo(task.getStatus());
        assertThat(taskDto.getCreatedAt()).isEqualTo(task.getCreatedAt());
        assertThat(taskDto.getUpdatedAt()).isEqualTo(task.getUpdatedAt());
        assertThat(taskDto.getDeadline()).isEqualTo(task.getDeadline());

        assertThat(taskDto.getAuthor().getId()).isEqualTo(task.getAuthor().getId());
        assertThat(taskDto.getAuthor().getFirstName()).isEqualTo(task.getAuthor().getFirstName());
        assertThat(taskDto.getAuthor().getLastName()).isEqualTo(task.getAuthor().getLastName());

        assertThat(taskDto.getAssignee().getId()).isEqualTo(task.getAssignee().getId());
        assertThat(taskDto.getAssignee().getFirstName()).isEqualTo(task.getAssignee().getFirstName());
        assertThat(taskDto.getAssignee().getLastName()).isEqualTo(task.getAssignee().getLastName());

        assertThat(taskDto.getViewers()).hasSameSizeAs(task.getViewers());
        assertThat(taskDto.getViewers().getFirst().getId()).isEqualTo(task.getViewers().getFirst().getId());
        assertThat(taskDto.getViewers().getFirst().getFirstName()).isEqualTo(task.getViewers().getFirst().getFirstName());
        assertThat(taskDto.getViewers().getFirst().getLastName()).isEqualTo(task.getViewers().getFirst().getLastName());
        assertThat(taskDto.getViewers().getLast().getId()).isEqualTo(task.getViewers().getLast().getId());
        assertThat(taskDto.getViewers().getLast().getFirstName()).isEqualTo(task.getViewers().getLast().getFirstName());
        assertThat(taskDto.getViewers().getLast().getLastName()).isEqualTo(task.getViewers().getLast().getLastName());
    }

    @Test
    @DisplayName("toDtoList should map List<Task> to List<TaskDto>")
    void toDtoList_ShouldMapListTaskToListTaskDto() {
        List<Task> taskList = List.of(createTask(1L), createTask(2L));
        List<TaskDto> taskDtoList = taskMapper.toDtoList(taskList);

        assertThat(taskDtoList).isNotNull().hasSize(2);
        assertThat(taskDtoList.get(0).getId()).isEqualTo(taskList.get(0).getId());
        assertThat(taskDtoList.get(1).getId()).isEqualTo(taskList.get(1).getId());
        assertThat(taskDtoList.get(0).getTitle()).isEqualTo(taskList.get(0).getTitle());
        assertThat(taskDtoList.get(1).getTitle()).isEqualTo(taskList.get(1).getTitle());
        assertThat(taskDtoList.get(0).getStatus()).isEqualTo(taskList.get(0).getStatus());
        assertThat(taskDtoList.get(1).getStatus()).isEqualTo(taskList.get(1).getStatus());
    }

    @Test
    @DisplayName("toEntity should map CreateTaskDto + Employee args to Task")
    void toEntity_ShouldMapCreateTaskDtoToTask() {
        CreateTaskDto dto = createCreateTaskDto();
        Employee author = createEmployee(1L);
        Employee assignee = createEmployee(2L);
        List<Employee> viewers = List.of(
                createEmployee(3L),
                createEmployee(4L)
        );

        Task task = taskMapper.toEntity(dto, author, assignee, viewers);

        assertThat(task).isNotNull();
        assertThat(task.getId()).isNull();
        assertThat(task.getTitle()).isEqualTo(dto.getTitle());
        assertThat(task.getDescription()).isEqualTo(dto.getDescription());
        assertThat(task.getStatus()).isEqualTo(dto.getStatus());
        assertThat(task.getDeadline()).isEqualTo(dto.getDeadline());
        assertThat(task.getCreatedAt()).isNull();
        assertThat(task.getUpdatedAt()).isNull();

        assertThat(task.getAuthor()).isEqualTo(author);
        assertThat(task.getAssignee()).isEqualTo(assignee);
        assertThat(task.getViewers()).hasSize(2);
        assertThat(task.getViewers()).containsExactlyElementsOf(viewers);
    }

    @Test
    @DisplayName("updateEntity should update existing Task from CreateTaskDto")
    void updateEntity_ShouldUpdateTaskFromCreateTaskDto() {
        Task existingTask = createTask(1L);
        CreateTaskDto updateDto = createCreateTaskDto();

        Employee newAuthor = createEmployee(5L)
                .setFirstName("newAuthorFirstName").setLastName("newAuthorLastName");
        Employee newAssignee = createEmployee(6L)
                .setFirstName("newAssigneeFirstName").setLastName("newAssigneeLastName");

        Task updatedTask = taskMapper.updateEntity(existingTask, updateDto, newAuthor, newAssignee);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getId()).isEqualTo(existingTask.getId());
        assertThat(updatedTask.getCreatedAt()).isEqualTo(existingTask.getCreatedAt());
        assertThat(updatedTask.getUpdatedAt()).isEqualTo(existingTask.getUpdatedAt());

        assertThat(updatedTask.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(updatedTask.getDescription()).isEqualTo(updateDto.getDescription());
        assertThat(updatedTask.getStatus()).isEqualTo(updateDto.getStatus());
        assertThat(updatedTask.getDeadline()).isEqualTo(updateDto.getDeadline());

        assertThat(updatedTask.getAuthor()).isEqualTo(newAuthor);
        assertThat(updatedTask.getAssignee()).isEqualTo(newAssignee);

        assertThat(updatedTask.getViewers()).isEqualTo(existingTask.getViewers());
    }

    private Employee createEmployee(Long id) {
        return Employee.builder()
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
                .viewers(List.of(createEmployee(3L), createEmployee(4L)))
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
}
