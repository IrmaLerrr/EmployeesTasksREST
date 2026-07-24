package org.irmalerrr.employeeservice.dto;

import lombok.*;
import lombok.experimental.Accessors;
import org.irmalerrr.employeeservice.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TaskStatus status;
    private EmployeeShortDto author; //todo -DONE- @saivanov: старайся держать структуру проекта, так: сначала обычные переменные, затем одиночные объекты, и только потом списки объектов(перепроверь остальные дто)
    private EmployeeShortDto assignee;
    private List<EmployeeShortDto> viewers;

}
