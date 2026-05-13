package org.irmalerrr.employeeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.irmalerrr.employeeservice.dto.CreateEmployeeDto;
import org.irmalerrr.employeeservice.dto.EmployeeDto;
import org.irmalerrr.employeeservice.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee")
public class EmployeeController {
    private final EmployeeService employeeService;
//    todo - DONE - всю логику и манипуляции из контроллера гнать в сервисный слой.

    //todo - DONE - у тебя в каждом методе повторяется api/employees, посмотри аннотацияю @RequestMapping
    @Operation(summary = "getEmployee (Возврат пользователя, если он существует)")
    @GetMapping(path = "/{id}") //todo - DONE - здесь и ниже как будто не хватает символа /
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(employeeService.getEmployeeDto(id));
    }

    @GetMapping
    @Operation(summary = "getAllEmployees (Возврат всех пользователей)")
    //todo - DONE - в наименованиях метода лучше добавлять -s на конце, если предполагается множественное число
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok().body(employeeService.getAllEmployeesDto());
    }

    @Operation(summary = "createEmployee (Создание пользователя)")
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody CreateEmployeeDto employee) {
        return ResponseEntity.ok().body(employeeService.createEmployee(employee));
    }

    @Operation(summary = "updateEmployee (Обновление данных пользователя)")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long id, @Valid @RequestBody CreateEmployeeDto employee) {
        return ResponseEntity.ok().body(employeeService.updateEmployee(id, employee));
    }

    @Operation(summary = "deleteEmployee (Удаление пользователя)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
