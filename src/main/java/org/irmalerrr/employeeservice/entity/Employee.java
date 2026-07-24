package org.irmalerrr.employeeservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "salary_gross")
    private BigDecimal salaryGross;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "position", nullable = false)
    private String position;

    @Builder.Default
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)//todo -DONE- @saivanov: лучше эту аннотацию указывать перед самим полем. т.к. она указывает на связь сущностей
    private List<Task> createdTasks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    private List<Task> assignedTasks = new ArrayList<>();

    @Builder.Default
    @ManyToMany(mappedBy = "viewers")
    private List<Task> viewedTasks = new ArrayList<>();
}
