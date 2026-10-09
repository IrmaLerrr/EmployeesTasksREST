package org.irmalerrr.employeeservice.service;

import org.irmalerrr.employeeservice.dto.CreateEmployeeDto;
import org.irmalerrr.employeeservice.dto.EmployeeDto;
import org.irmalerrr.employeeservice.entity.Employee;
import org.irmalerrr.employeeservice.entity.Task;
import org.irmalerrr.employeeservice.exceptions.EmployeeNotFoundException;
import org.irmalerrr.employeeservice.mapper.EmployeeMapper;
import org.irmalerrr.employeeservice.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    @DisplayName("getEmployee should return EmployeeDto when employee exists")
    void getEmployee_ShouldReturnEmployeeDto() {
        Employee expectedEntity = createEmployee(1L);
        EmployeeDto expectedDto = createEmployeeDto(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(expectedEntity));
        when(employeeMapper.toDto(expectedEntity)).thenReturn(expectedDto);

        EmployeeDto actualDto = employeeService.getEmployee(1L);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(employeeRepository).findById(1L);
        verify(employeeMapper).toDto(expectedEntity);
    }

    @Test
    @DisplayName("getEmployee should throw EmployeeNotFoundException when employee not found")
    void getEmployee_ShouldThrowEmployeeNotFoundException() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployee(999L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with id: 999");

        verify(employeeRepository).findById(999L);
        verify(employeeMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("getAllEmployees should return list of EmployeeDto")
    void getAllEmployees_ShouldReturnListOfEmployee() {
        List<Employee> employees = List.of(createEmployee(1L), createEmployee(2L));
        List<EmployeeDto> expectedDtos = List.of(createEmployeeDto(1L), createEmployeeDto(2L));

        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toDtoList(employees)).thenReturn(expectedDtos);

        List<EmployeeDto> actualDtos = employeeService.getAllEmployees();

        assertThat(actualDtos).hasSize(2)
                .isEqualTo(expectedDtos);
        verify(employeeRepository).findAll();
        verify(employeeMapper).toDtoList(employees);
    }

    @Test
    @DisplayName("getAllEmployees should return empty list when no employees")
    void getAllEmployees_ShouldReturnEmptyList() {
        when(employeeRepository.findAll()).thenReturn(List.of());
        when(employeeMapper.toDtoList(List.of())).thenReturn(List.of());

        List<EmployeeDto> actualDtos = employeeService.getAllEmployees();

        assertThat(actualDtos).isEmpty();
        verify(employeeRepository).findAll();
        verify(employeeMapper).toDtoList(List.of());
    }

    @Test
    @DisplayName("createEmployee should create and return EmployeeDto")
    void createEmployee_ShouldCreateAndReturnEmployeeDto() {
        CreateEmployeeDto dto = createCreateEmployeeDto();
        Employee employeeToSave = createEmployee(null);
        Employee savedEmployee = createEmployee(1L);
        EmployeeDto expectedDto = createEmployeeDto(1L);

        when(employeeMapper.toEntity(dto)).thenReturn(employeeToSave);
        when(employeeRepository.save(employeeToSave)).thenReturn(savedEmployee);
        when(employeeMapper.toDto(savedEmployee)).thenReturn(expectedDto);

        EmployeeDto actualDto = employeeService.createEmployee(dto);

        assertThat(actualDto).isEqualTo(expectedDto);
        assertThat(actualDto.getId()).isEqualTo(1L);
        verify(employeeMapper).toEntity(dto);
        verify(employeeRepository).save(employeeToSave);
        verify(employeeMapper).toDto(savedEmployee);
    }

    @Test
    @DisplayName("updateEmployee should update and return EmployeeDto when employee exists")
    void updateEmployee_ShouldUpdateAndReturnEmployeeDto() {
        Long employeeId = 1L;
        CreateEmployeeDto dto = createCreateEmployeeDto();
        Employee existingEmployee = createEmployee(employeeId);
        Employee updatedEmployee = createEmployee(employeeId);
        EmployeeDto expectedDto = createEmployeeDto(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeMapper.updateEntity(existingEmployee, dto)).thenReturn(updatedEmployee);
        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);
        when(employeeMapper.toDto(updatedEmployee)).thenReturn(expectedDto);

        EmployeeDto actualDto = employeeService.updateEmployee(employeeId, dto);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper).updateEntity(existingEmployee, dto);
        verify(employeeRepository).save(updatedEmployee);
        verify(employeeMapper).toDto(updatedEmployee);
    }

    @Test
    @DisplayName("updateEmployee should throw EmployeeNotFoundException when employee not found")
    void updateEmployee_ShouldThrowEmployeeNotFoundException() {
        Long employeeId = 0L;
        CreateEmployeeDto dto = createCreateEmployeeDto();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(employeeId, dto))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with id: " + employeeId);

        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper, never()).updateEntity(any(), any());
        verify(employeeRepository, never()).save(any());
        verify(employeeMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("deleteEmployee should delete employee when employee exists")
    void deleteEmployee_ShouldDeleteEmployee() {
        Long employeeId = 1L;
        Employee employee = createEmployee(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).delete(employee);
    }

    @Test
    @DisplayName("deleteEmployee should throw EmployeeNotFoundException when employee not found")
    void deleteEmployee_ShouldThrowEmployeeNotFoundException() {
        Long employeeId = 0L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployee(employeeId))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with id: " + employeeId);

        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository, never()).delete(any());
    }

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

    private EmployeeDto createEmployeeDto(Long id) {
        return EmployeeDto.builder()
                .id(id)
                .firstName("firstName")
                .lastName("lastName")
                .email("test@test.test")
                .salaryGross(BigDecimal.valueOf(10000))
                .phoneNumber("+71234567890")
                .position("position")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
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
}