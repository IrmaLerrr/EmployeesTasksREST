package taskmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.dto.TaskDTO;
import taskmanagement.service.TaskService;

import java.util.NoSuchElementException;

@RestController
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(path = "api/tasks/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok().body(taskService.getTask(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "api/tasks")
    public ResponseEntity<?> getAllTask(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(taskService.getAllTask());
    }

    @PostMapping("api/tasks")
    public ResponseEntity<?> createTask(@RequestBody TaskDTO task) {
        return ResponseEntity.ok().body(taskService.createTask(task));
    }

    @PutMapping("api/tasks/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") Integer id, @RequestBody TaskDTO task) {
        try {
            return ResponseEntity.ok().body(taskService.updateTask(id, task));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("api/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Integer id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}


