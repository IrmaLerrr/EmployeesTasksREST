package taskmanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTORequest {
    //todo EmployeeDto, припискеи Request и Response особо не нужны, чтоб понятнее для чего эта ДТО можно указывать действие в названии - CreateEmployeeDto. или UpdateEmployeeDto(в случае если набор полей разный для создания и редакирования)
    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "lastName is required")
    private String lastName;

    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Invalid email format"
    )
    private String email;

    @Min(value = 0, message = "salary can not be less than 0")
    private Integer salaryGross;

    private String phoneNumber; //todo валидация

    @NotBlank(message = "position is required")
    private String position;


}
