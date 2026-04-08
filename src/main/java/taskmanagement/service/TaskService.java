package taskmanagement.service;

import org.springframework.stereotype.Service;
import taskmanagement.dto.TaskDTO;
import taskmanagement.model.Task;
import taskmanagement.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getTask(Integer id) {
//        TODO
        return null;
    }

    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    public Task createTask(TaskDTO employee) {
        //        TODO
        return null;
    }

    public Task updateTask(Integer id, TaskDTO employee) {
        //        TODO
        return null;
    }

    public void deleteTask(Integer id) {
        //        TODO
        return;
    }
}

