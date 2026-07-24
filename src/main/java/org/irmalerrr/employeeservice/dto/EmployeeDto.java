package org.irmalerrr.employeeservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal salaryGross;
}
