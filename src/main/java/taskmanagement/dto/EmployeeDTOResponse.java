package taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTOResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer salaryGross;
    private String phoneNumber;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
