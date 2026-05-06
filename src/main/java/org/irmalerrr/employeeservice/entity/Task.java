package org.irmalerrr.employeeservice.entity;

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

    //todo - DONE - сначала располагаем поля-обычные колонки, затем поля-связи
    @Column(name = "deadline")
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "author")
    private Employee author;

    @ManyToOne
    @JoinColumn(name = "assignee")
    private Employee assignee;

    @ManyToMany
    @JoinTable(
            name = "task_viewers",  // новая таблица
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> viewers = new ArrayList<>();

}
