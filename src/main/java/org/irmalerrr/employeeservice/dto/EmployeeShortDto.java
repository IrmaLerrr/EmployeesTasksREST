package org.irmalerrr.employeeservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//todo - DONE - EmployeeShortDTO
public class EmployeeShortDto {
    private Long id;
    private String firstName;
    private String lastName;
}
