package org.irmalerrr.employeeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.irmalerrr.employeeservice.dto.CreateTaskDto;
import org.irmalerrr.employeeservice.dto.TaskDto;
import org.irmalerrr.employeeservice.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/tasks") //todo @saivanov: "/api/tasks"
@RequiredArgsConstructor
@Tag(name = "TaskController", description = "Контроллер для управления задачами")
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Получение задачи по идентификатору")
    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(taskService.getTask(id));
    }

    @Operation(summary = "Получение всех задач")
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok().body(taskService.getAllTasks());
    }

    @Operation(summary = "Создание задачи")
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskDto taskReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskReq));
    }

    @Operation(summary = "Обновление данных задачи")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") Long id, @Valid @RequestBody CreateTaskDto taskReq) {
        return ResponseEntity.ok().body(taskService.updateTask(id, taskReq));
    }

    @Operation(summary = "Удаление задачи")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}


