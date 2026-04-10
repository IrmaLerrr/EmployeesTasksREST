package taskmanagement.service;

import org.springframework.stereotype.Service;
import taskmanagement.dto.EmployeeDTORequest;
import taskmanagement.model.Employee;
import taskmanagement.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DtoMapper mapper;

    public EmployeeService(EmployeeRepository employeeRepository, DtoMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }

    public Employee getEmployee(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + id));
    }

    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(EmployeeDTORequest employee) {
        return employeeRepository.save(mapper
                .dtoToEntity(employee)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
        );
    }

    public Employee updateEmployee(Integer id, EmployeeDTORequest employee) {
        Employee target = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + id));
        target.setFirstName(employee.getFirstName())
                .setLastName(employee.getLastName())
                .setEmail(employee.getEmail())
                .setSalaryGross(employee.getSalaryGross())
                .setPhoneNumber(employee.getPhoneNumber())
                .setPosition(employee.getPosition())
                .setUpdatedAt(LocalDateTime.now());
        return employeeRepository.save(target);
    }

    public void deleteEmployee(Integer id) {
        Employee target = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + id));
        employeeRepository.delete(target);
    }
}
