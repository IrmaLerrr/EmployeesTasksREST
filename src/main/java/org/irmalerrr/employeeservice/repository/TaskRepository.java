package org.irmalerrr.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.irmalerrr.employeeservice.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}

