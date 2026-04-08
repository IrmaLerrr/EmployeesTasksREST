package taskmanagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import taskmanagement.model.Employee;
import taskmanagement.model.Task;
import taskmanagement.model.TaskStatus;
import taskmanagement.repository.EmployeeRepository;
import taskmanagement.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Component
    class Runner implements CommandLineRunner {
        private final EmployeeRepository employeeRepository;
        private final TaskRepository taskRepository;

        public Runner(EmployeeRepository employeeRepository, TaskRepository taskRepository) {
            this.employeeRepository = employeeRepository;
            this.taskRepository = taskRepository;
        }

        @Override
        @Transactional
        public void run(String... args) {
            log.info("Creating Test Data");
            Employee author = Employee.builder()
                    .firstName("Иван")
                    .lastName("Петров")
                    .email("ivan.petrov@example.com")
                    .phoneNumber("+7 (999) 123-45-67")
                    .position("Team Lead")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Employee assignee = Employee.builder()
                    .firstName("Мария")
                    .lastName("Сидорова")
                    .email("maria.sidorova@example.com")
                    .phoneNumber("+7 (999) 765-43-21")
                    .position("Developer")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Employee savedAuthor = employeeRepository.save(author);
            Employee savedAssignee = employeeRepository.save(assignee);

            Task task = Task.builder()
                    .title("Реализовать функционал аутентификации")
                    .description("Необходимо реализовать JWT аутентификацию для REST API")
                    .status(TaskStatus.OPEN)
                    .author(savedAuthor)
                    .assignee(savedAssignee)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Task savedTask = taskRepository.save(task);
        }
    }
}
