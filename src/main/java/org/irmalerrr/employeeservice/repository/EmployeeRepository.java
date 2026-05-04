package org.irmalerrr.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.irmalerrr.employeeservice.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
