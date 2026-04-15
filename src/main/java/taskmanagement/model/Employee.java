package taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@JsonPropertyOrder({"id", "title", "description", "status", "author"})
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String email;

    @Column(name = "salary_gross")
    private Integer salaryGross;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Task> created_tasks = new ArrayList<>();

    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    private List<Task> assigned_tasks = new ArrayList<>();


}
