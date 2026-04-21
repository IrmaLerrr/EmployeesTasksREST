package org.irmalerrr.employeeservice.model; //todo лучше всего вместо model сделай entity

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//todo - DONE - @Data содердит много чего в себе, тебе все нужны методы которые генерятся этой аннатацией? лучше вешать только те аннатации которые тебе нужны для текущей реализации)
//todo - DONE - а зачем @JsonPropertyOrder({"id", "title", "description", "status", "author"}) ? - для красоты вывода джсона, перенесла в дто
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity //todo - DONE - @Entity и @Table пишут прям над классом, по ближе к названию для читаемости. добавь @Table с названием таблицы
@Table(name = "employee")
public class Employee extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email; //todo - DONE - для ентити лучше на всех полях указать поле на которое ссылаемся в таблице(кроме айди)

    //todo - DONE - зп может быть с копейкам) запомни все что касается денег это BigDecimal)
    @Column(name = "salary_gross")
    private BigDecimal salaryGross;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "position", nullable = false)
    private String position;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Task> createdTasks = new ArrayList<>(); //todo - DONE - кемел кейс юзаем

    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    private List<Task> assignedTasks = new ArrayList<>(); //todo - DONE - кемел кейс юзаем

    // todo - DONE - сделать связь мени ту мени с наблюдателями, через интерсект таблица
    @ManyToMany
    @JoinTable(
            name = "task_viewers",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Task> viewedTasks = new ArrayList<>();
}
