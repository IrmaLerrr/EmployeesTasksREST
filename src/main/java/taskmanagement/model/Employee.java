package taskmanagement.model; //todo лучше всего вместо model сделай entity

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity //todo @Entity и @Table пишут прям над классом, по ближе к названию для читаемости. добавь @Table с названием таблицы
@Data //todo: data содердит много чего в себе, тебе все нужны методы которые генерятся этой аннатацией? лучше вешать только те аннатации которые тебе нужны для текущей реализации)
@JsonPropertyOrder({"id", "title", "description", "status", "author"}) //todo а зачем?
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //todo у тебя есть 3 повторяющийхся поля в классах. можно сделать абстрактный класс BaseEntity и отнаследоваться)

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String email; //todo для ентити лучше на всех полях указать поле на которое ссылаемся в таблице(кроме айди)

    //todo зп может быть с копейкам) запомни все что касается денег это BigDecimal)
    @Column(name = "salary_gross")
    private Integer salaryGross;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "position", nullable = false)
    private String position;

    // todo @CreatedDate  @LastModifiedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Task> created_tasks = new ArrayList<>(); //todo кемел кейс юзаем


    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    private List<Task> assigned_tasks = new ArrayList<>();

    // todo сделать свазь мени ту мени с наблюдателями, через интерсект таблица

}
