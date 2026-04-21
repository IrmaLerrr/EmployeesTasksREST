package org.irmalerrr.employeeservice.dto;

import lombok.*;
import org.irmalerrr.employeeservice.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private EmployeeShortDto author;
    private EmployeeShortDto assignee;
    private List<EmployeeShortDto> viewers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate deadline;

}
