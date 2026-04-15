package taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import taskmanagement.model.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTOResponse {
    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private EmployeeDTOResponseShort author;
    private EmployeeDTOResponseShort assignee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate deadline;

}
