package org.irmalerrr.employeeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//todo - DONE - EmployeeShortDTO
public class EmployeeShortDto {
    private Long id;
    private String firstName;
    private String lastName;
}
