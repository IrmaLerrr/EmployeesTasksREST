package taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import taskmanagement.model.TaskStatus;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    @NotBlank(message = "title is required")
    private String title;

    private String description;

    private TaskStatus status;

    @NotBlank(message = "author is required")
    private Integer author_id;

    private Integer assignee_id;

    private LocalDate deadline;

}
