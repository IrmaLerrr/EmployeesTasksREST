package taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanagement.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
