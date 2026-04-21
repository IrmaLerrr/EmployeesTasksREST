package org.irmalerrr.employeeservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.irmalerrr.employeeservice.dto.CreateTaskDto;
import org.irmalerrr.employeeservice.dto.TaskDto;
import org.irmalerrr.employeeservice.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
//    private final DtoMapper mapper;
    // todo - DONE - Посмотри аннатацию @RequiredArgsConstructor

    @GetMapping(path = "{id}")
    // todo - DONE - ResponseEntity<?> не красиво возвращать) лучше класс обьекта укажи который возвращается
    public ResponseEntity<TaskDto> getTask(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(taskService.getTask(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTask() {
        return ResponseEntity.ok().body(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskDto taskReq) {
        return ResponseEntity.ok().body(taskService.createTask(taskReq));
    }

    @PutMapping("{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") Long id, @Valid @RequestBody CreateTaskDto taskReq) {
        return ResponseEntity.ok().body(taskService.updateTask(id, taskReq));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}


