package org.irmalerrr.employeeservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.irmalerrr.employeeservice.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDto {
    @NotBlank(message = "title is required")
    private String title;

    private String description;

    private TaskStatus status;

    @NotNull(message = "author is required")
    private Long author;

    private Long assignee;

    private List<Long> viewers;

    private LocalDate deadline;

}
