package org.irmalerrr.employeeservice.mapper;

import org.irmalerrr.employeeservice.dto.CreateEmployeeDto;
import org.irmalerrr.employeeservice.dto.EmployeeDto;
import org.irmalerrr.employeeservice.dto.EmployeeShortDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {EmployeeMapperImpl.class})
class EmployeeMapperTests {
    @Autowired
    private EmployeeMapper employeeMapper;

    private Employee createEmployee(Long id) {
        Task task = Task.builder().id(1L).title("title").build();
        return Employee.builder()
                .id(id)
                .firstName("firstName")
                .lastName("lastName")
                .email("test@test.test")
                .salaryGross(BigDecimal.valueOf(10000))
                .phoneNumber("+71234567890")
                .position("position")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdTasks(List.of(task))
                .assignedTasks(List.of(task))
                .viewedTasks(List.of(task))
                .build();
    }

    private CreateEmployeeDto createCreateEmployeeDto() {
        return CreateEmployeeDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("test@test.test")
                .salaryGross(BigDecimal.valueOf(10000))
                .phoneNumber("+71234567890")
                .position("position")
                .build();
    }

    @Test
    @DisplayName("toDto should map Employee to EmployeeDto")
    void toDto_ShouldMapEmployeeToEmployeeDto() {
        Employee employee = createEmployee(1L);
        EmployeeDto employeeDto = employeeMapper.toDto(employee);

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(employee.getId());
        assertThat(employeeDto.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(employeeDto.getLastName()).isEqualTo(employee.getLastName());
        assertThat(employeeDto.getEmail()).isEqualTo(employee.getEmail());
        assertThat(employeeDto.getSalaryGross()).isEqualTo(employee.getSalaryGross());
        assertThat(employeeDto.getPhoneNumber()).isEqualTo(employee.getPhoneNumber());
        assertThat(employeeDto.getPosition()).isEqualTo(employee.getPosition());
        assertThat(employeeDto.getCreatedAt()).isEqualTo(employee.getCreatedAt());
        assertThat(employeeDto.getUpdatedAt()).isEqualTo(employee.getUpdatedAt());
    }

    @Test
    @DisplayName("toDtoList should map List<Employee> to List<EmployeeDto>")
    void toDtoList_ShouldMapListEmployeeToListEmployeeShortDto() {
        List<Employee> employeeList = List.of(createEmployee(1L), createEmployee(2L));
        List<EmployeeDto> employeeDtoList = employeeMapper.toDtoList(employeeList);

        assertThat(employeeDtoList).isNotNull().hasSize(2);
        assertThat(employeeDtoList.getFirst().getId()).isEqualTo(employeeList.getFirst().getId());
        assertThat(employeeDtoList.getLast().getId()).isEqualTo(employeeList.getLast().getId());
        assertThat(employeeDtoList.getFirst().getFirstName()).isEqualTo(employeeList.getFirst().getFirstName());
        assertThat(employeeDtoList.getLast().getFirstName()).isEqualTo(employeeList.getLast().getFirstName());
        assertThat(employeeDtoList.getFirst().getLastName()).isEqualTo(employeeList.getFirst().getLastName());
        assertThat(employeeDtoList.getLast().getLastName()).isEqualTo(employeeList.getLast().getLastName());
        assertThat(employeeDtoList.getFirst().getEmail()).isEqualTo(employeeList.getFirst().getEmail());
        assertThat(employeeDtoList.getLast().getEmail()).isEqualTo(employeeList.getLast().getEmail());
        assertThat(employeeDtoList.getFirst().getSalaryGross()).isEqualTo(employeeList.getFirst().getSalaryGross());
        assertThat(employeeDtoList.getLast().getSalaryGross()).isEqualTo(employeeList.getLast().getSalaryGross());
        assertThat(employeeDtoList.getFirst().getPhoneNumber()).isEqualTo(employeeList.getFirst().getPhoneNumber());
        assertThat(employeeDtoList.getLast().getPhoneNumber()).isEqualTo(employeeList.getLast().getPhoneNumber());
        assertThat(employeeDtoList.getFirst().getPosition()).isEqualTo(employeeList.getFirst().getPosition());
        assertThat(employeeDtoList.getLast().getPosition()).isEqualTo(employeeList.getLast().getPosition());
        assertThat(employeeDtoList.getFirst().getCreatedAt()).isEqualTo(employeeList.getFirst().getCreatedAt());
        assertThat(employeeDtoList.getLast().getCreatedAt()).isEqualTo(employeeList.getLast().getCreatedAt());
        assertThat(employeeDtoList.getFirst().getUpdatedAt()).isEqualTo(employeeList.getFirst().getUpdatedAt());
        assertThat(employeeDtoList.getLast().getUpdatedAt()).isEqualTo(employeeList.getLast().getUpdatedAt());

    }

    @Test
    @DisplayName("toShortDto should map Employee to EmployeeShortDto")
    void toShortDto_ShouldMapEmployeeToEmployeeShortDto() {
        Employee employee = createEmployee(1L);
        EmployeeShortDto employeeDto = employeeMapper.toShortDto(employee);

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(employee.getId());
        assertThat(employeeDto.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(employeeDto.getLastName()).isEqualTo(employee.getLastName());

    }

    @Test
    @DisplayName("toShortDtoList should map List<Employee> to List<EmployeeShortDto>")
    void toShortDtoList_ShouldMapListEmployeeToListEmployeeShortDto() {
        List<Employee> employeeList = List.of(createEmployee(1L), createEmployee(2L));
        List<EmployeeShortDto> employeeDtoList = employeeMapper.toShortDtoList(employeeList);

        assertThat(employeeDtoList).isNotNull().hasSize(2);
        assertThat(employeeDtoList.getFirst().getId()).isEqualTo(employeeList.getFirst().getId());
        assertThat(employeeDtoList.getLast().getId()).isEqualTo(employeeList.getLast().getId());
        assertThat(employeeDtoList.getFirst().getFirstName()).isEqualTo(employeeList.getFirst().getFirstName());
        assertThat(employeeDtoList.getLast().getFirstName()).isEqualTo(employeeList.getLast().getFirstName());
        assertThat(employeeDtoList.getFirst().getLastName()).isEqualTo(employeeList.getFirst().getLastName());
        assertThat(employeeDtoList.getLast().getLastName()).isEqualTo(employeeList.getLast().getLastName());

    }

    @Test
    @DisplayName("toEntity should map CreateEmployeeDto to Employee")
    void toEntity_ShouldMapCreateEmployeeDtoToEmployee() {
        CreateEmployeeDto employeeDto = createCreateEmployeeDto();
        Employee employee = employeeMapper.toEntity(employeeDto);

        assertThat(employee).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo(employeeDto.getFirstName());
        assertThat(employee.getLastName()).isEqualTo(employeeDto.getLastName());
        assertThat(employee.getEmail()).isEqualTo(employeeDto.getEmail());
        assertThat(employee.getSalaryGross()).isEqualTo(employeeDto.getSalaryGross());
        assertThat(employee.getPhoneNumber()).isEqualTo(employeeDto.getPhoneNumber());
        assertThat(employee.getPosition()).isEqualTo(employeeDto.getPosition());
        assertThat(employee.getCreatedAt()).isNull();
        assertThat(employee.getUpdatedAt()).isNull();
        assertThat(employee.getAssignedTasks()).isEmpty();
        assertThat(employee.getCreatedTasks()).isEmpty();
        assertThat(employee.getViewedTasks()).isEmpty();
    }

    @Test
    @DisplayName("updateEntity should update existing Employee from CreateEmployeeDto")
    void updateEntity_ShouldUpdateEmployeeFromCreateEmployeeDto() {
        CreateEmployeeDto employeeDto = createCreateEmployeeDto();
        employeeDto.setFirstName("otherFirstName")
                .setLastName("otherLastName")
                .setEmail("otherTest@test.test")
                .setSalaryGross(BigDecimal.valueOf(20000))
                .setPosition("otherPosition");

        Employee employee = createEmployee(1L);
        Employee updatedEmployee = employeeMapper.updateEntity(employee, employeeDto);

        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(employee.getId());
        assertThat(updatedEmployee.getCreatedAt()).isEqualTo(employee.getCreatedAt());
        assertThat(updatedEmployee.getUpdatedAt()).isEqualTo(employee.getUpdatedAt());
        assertThat(updatedEmployee.getAssignedTasks()).isEqualTo(employee.getAssignedTasks());
        assertThat(updatedEmployee.getCreatedTasks()).isEqualTo(employee.getCreatedTasks());
        assertThat(updatedEmployee.getViewedTasks()).isEqualTo(employee.getViewedTasks());
        assertThat(updatedEmployee.getFirstName()).isEqualTo(employeeDto.getFirstName());
        assertThat(updatedEmployee.getLastName()).isEqualTo(employeeDto.getLastName());
        assertThat(updatedEmployee.getEmail()).isEqualTo(employeeDto.getEmail());
        assertThat(updatedEmployee.getSalaryGross()).isEqualTo(employeeDto.getSalaryGross());
        assertThat(updatedEmployee.getPhoneNumber()).isEqualTo(employeeDto.getPhoneNumber());
        assertThat(updatedEmployee.getPosition()).isEqualTo(employeeDto.getPosition());
    }
}
