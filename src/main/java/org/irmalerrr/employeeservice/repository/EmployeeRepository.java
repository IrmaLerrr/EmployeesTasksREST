package org.irmalerrr.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.irmalerrr.employeeservice.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
