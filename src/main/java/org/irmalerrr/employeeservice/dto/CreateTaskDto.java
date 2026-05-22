package org.irmalerrr.employeeservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import org.irmalerrr.employeeservice.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDto {
    @NotBlank(message = "title is required")
    private String title;

    private String description;

    private TaskStatus status;

    //todo - DONE - лучше использовать наименование authorId, аналогично для assignee и viewers
    @NotNull(message = "author is required")
    private Long authorId;

    private Long assigneeId;

    private List<Long> viewersIds;

    private LocalDate deadline;

}
