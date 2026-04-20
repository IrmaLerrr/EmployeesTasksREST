package taskmanagement.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.dto.TaskDTORequest;
import taskmanagement.dto.TaskDTOResponse;
import taskmanagement.service.DtoMapper;
import taskmanagement.service.TaskService;

import java.util.List;

@RestController

public class TaskController {
    private final TaskService taskService;
    private final DtoMapper mapper;
    // todo Посмотри аннатацию @RequiredArgsConstructor
    public TaskController(TaskService taskService, DtoMapper mapper) {
        this.taskService = taskService;
        this.mapper = mapper;
    }

    @GetMapping(path = "api/tasks/{id}")
    // todo ResponseEntity<?> не красиво возвращать) лучше класс обьекта укажи который возвращается
    public ResponseEntity<?> getTask(@PathVariable("id") Integer id) {
        TaskDTOResponse task = mapper.entityToDto(taskService.getTask(id));
        return ResponseEntity.ok().body(task);
    }

    @GetMapping(path = "api/tasks")
    public ResponseEntity<?> getAllTask() {
        List<TaskDTOResponse> task = mapper.entityToDtoTask(taskService.getAllTasks());
        return ResponseEntity.ok().body(task);
    }

    @PostMapping("api/tasks")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDTORequest taskReq) {
        TaskDTOResponse task = mapper.entityToDto(taskService.createTask(taskReq));
        return ResponseEntity.ok().body(task);
    }

    @PutMapping("api/tasks/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") Integer id, @Valid @RequestBody TaskDTORequest taskReq) {
        TaskDTOResponse task = mapper.entityToDto(taskService.updateTask(id, taskReq));
        return ResponseEntity.ok().body(task);
    }

    @DeleteMapping("api/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Integer id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}


