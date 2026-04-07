package taskmanagement.service;

import org.springframework.stereotype.Service;
import taskmanagement.model.Employee;
import taskmanagement.model.Task;
import taskmanagement.model.TaskStatus;
import taskmanagement.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Integer id, String title, TaskStatus status, Employee author, Employee assignee, String description) {
        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(status)
                .author(author)
                .assignee(assignee)
                .build();
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}

