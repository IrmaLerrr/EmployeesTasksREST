package org.irmalerrr.employeeservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDto {
    //todo - DONE - EmployeeDto, приписки Request и Response особо не нужны, чтоб понятнее для чего эта ДТО можно указывать действие в названии - CreateEmployeeDto. или UpdateEmployeeDto(в случае если набор полей разный для создания и редакирования)
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
    private BigDecimal salaryGross;

    @Pattern(
            regexp = "^(\\+7|8)[0-9]{10}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber; //todo - DONE - валидация

    @NotBlank(message = "position is required")
    private String position;


}
