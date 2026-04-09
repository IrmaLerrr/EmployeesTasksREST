package taskmanagement;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import taskmanagement.model.Employee;
import taskmanagement.model.Task;
import taskmanagement.model.TaskStatus;
import taskmanagement.repository.EmployeeRepository;
import taskmanagement.repository.TaskRepository;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
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
            Employee author = new Employee()
                    .setFirstName("Иван")
                    .setLastName("Петров")
                    .setEmail("ivan.petrov@example.com")
                    .setPhoneNumber("+7 (999) 123-45-67")
                    .setPosition("Team Lead")
                    .setCreatedAt(LocalDateTime.now())
                    .setUpdatedAt(LocalDateTime.now());

            Employee assignee = new Employee()
                    .setFirstName("Мария")
                    .setLastName("Сидорова")
                    .setEmail("maria.sidorova@example.com")
                    .setPhoneNumber("+7 (999) 765-43-21")
                    .setPosition("Developer")
                    .setCreatedAt(LocalDateTime.now())
                    .setUpdatedAt(LocalDateTime.now());

            Employee savedAuthor = employeeRepository.save(author);
            Employee savedAssignee = employeeRepository.save(assignee);

            Task task = new Task()
                    .setTitle("Реализовать функционал аутентификации")
                    .setDescription("Необходимо реализовать JWT аутентификацию для REST API")
                    .setStatus(TaskStatus.OPEN)
                    .setAuthor(savedAuthor)
                    .setAssignee(savedAssignee)
                    .setCreatedAt(LocalDateTime.now())
                    .setUpdatedAt(LocalDateTime.now());

            Task savedTask = taskRepository.save(task);
        }
    }
}
