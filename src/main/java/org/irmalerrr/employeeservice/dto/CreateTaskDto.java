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
    @NotNull(message = "authorId is required") //todo -DONE- @saivanov:  authorId наверное
    private Long authorId;

    private Long assigneeId;

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    private LocalDate deadline;

    private TaskStatus status;

    private List<Long> viewersIds;
}
