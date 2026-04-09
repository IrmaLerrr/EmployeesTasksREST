package taskmanagement.service;

import org.springframework.stereotype.Service;
import taskmanagement.dto.TaskDTO;
import taskmanagement.model.Employee;
import taskmanagement.model.Task;
import taskmanagement.repository.EmployeeRepository;
import taskmanagement.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final DtoMapper mapper;

    public TaskService(TaskRepository taskRepository, DtoMapper mapper, EmployeeRepository employeeRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }

    public Task getTask(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
    }

    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    public Task createTask(TaskDTO taskDTO) {
        Integer author_id = taskDTO.getAuthor_id();
        Integer assignee_id = taskDTO.getAssignee_id();
        Employee author = employeeRepository.findById(author_id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + author_id));
        Employee assignee = assignee_id == null ? null : employeeRepository.findById(assignee_id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + assignee_id));

        return taskRepository.save(mapper
                .dtoToEntity(taskDTO)
                .setAuthor(author)
                .setAssignee(assignee)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
        );
    }

    public Task updateTask(Integer id, TaskDTO task) {
        Task target = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));

        Integer author_id = task.getAuthor_id();
        Integer assignee_id = task.getAssignee_id();
        Employee author = employeeRepository.findById(author_id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + author_id));
        Employee assignee = assignee_id == null ? null : employeeRepository.findById(assignee_id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + assignee_id));

        target.setTitle(task.getTitle())
                .setDescription(task.getDescription())
                .setStatus(task.getStatus())
                .setAuthor(author)
                .setAssignee(assignee)
                .setDeadline(task.getDeadline())
                .setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(target);
    }

    public void deleteTask(Integer id) {
        Task target = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
        taskRepository.delete(target);
    }
}

