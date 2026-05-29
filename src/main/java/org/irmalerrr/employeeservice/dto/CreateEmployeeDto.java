package org.irmalerrr.employeeservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDto {

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
            regexp = "^(\\+7|8)\\d{10}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;

    @NotBlank(message = "position is required")
    private String position;


}
