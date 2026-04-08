package taskmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.dto.EmployeeDTO;
import taskmanagement.service.EmployeeService;

import java.util.NoSuchElementException;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "api/employees/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok().body(employeeService.getEmployee(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "api/employees")
    public ResponseEntity<?> getAllEmployee(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(employeeService.getAllEmployee());
    }

    @PostMapping("api/employees")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO employee) {
        return ResponseEntity.ok().body(employeeService.createEmployee(employee));
    }

    @PutMapping("api/employees/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") Integer id, @RequestBody EmployeeDTO employee) {
        try {
            return ResponseEntity.ok().body(employeeService.updateEmployee(id, employee));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("api/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Integer id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
