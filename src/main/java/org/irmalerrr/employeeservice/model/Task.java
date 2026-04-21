package org.irmalerrr.employeeservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.irmalerrr.employeeservice.enums.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "author")
    private Employee author;

    @ManyToOne
    @JoinColumn(name = "assignee")
    private Employee assignee;

    @ManyToMany(mappedBy = "viewedTasks")
    private List<Employee> viewers = new ArrayList<>();

    @Column(name = "deadline")
    private LocalDate deadline;
}
