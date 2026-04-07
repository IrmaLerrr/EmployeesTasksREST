package taskmanagement.controller;

import org.springframework.web.bind.annotation.*;
import taskmanagement.service.TaskService;

@RestController
public class EmployeeController {
    private final TaskService taskService;

    public EmployeeController(TaskService taskService) {
        this.taskService = taskService;
    }

}
