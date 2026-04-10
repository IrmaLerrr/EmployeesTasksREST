package taskmanagement.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.dto.EmployeeDTORequest;
import taskmanagement.dto.EmployeeDTOResponse;
import taskmanagement.service.DtoMapper;
import taskmanagement.service.EmployeeService;

import java.util.List;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DtoMapper mapper;

    public EmployeeController(EmployeeService employeeService, DtoMapper mapper) {
        this.employeeService = employeeService;
        this.mapper = mapper;
    }

    @GetMapping(path = "api/employees/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable("id") Integer id) {
        EmployeeDTOResponse emp = mapper.entityToDto(employeeService.getEmployee(id));
        return ResponseEntity.ok().body(emp);
    }

    @GetMapping(path = "api/employees")
    public ResponseEntity<?> getAllEmployee() {
        List<EmployeeDTOResponse> emp = mapper.entityToDto(employeeService.getAllEmployee());
        return ResponseEntity.ok().body(emp);
    }

    @PostMapping("api/employees")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeDTORequest employee) {
        EmployeeDTOResponse emp = mapper.entityToDto(employeeService.createEmployee(employee));
        return ResponseEntity.ok().body(emp);
    }

    @PutMapping("api/employees/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") Integer id, @Valid @RequestBody EmployeeDTORequest employee) {
        EmployeeDTOResponse emp = mapper.entityToDto(employeeService.updateEmployee(id, employee));
        return ResponseEntity.ok().body(emp);
    }

    @DeleteMapping("api/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Integer id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
