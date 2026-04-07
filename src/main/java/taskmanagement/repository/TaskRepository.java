package taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanagement.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}

