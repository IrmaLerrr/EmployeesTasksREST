package taskmanagement.service;

import org.springframework.stereotype.Service;
import taskmanagement.dto.EmployeeDTO;
import taskmanagement.model.Employee;
import taskmanagement.repository.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee getEmployee(Integer id) {
//        TODO
        return null;
    }

    public Employee getAllEmployee() {
        //        TODO
        return null;
    }

    public Employee createEmployee(EmployeeDTO employee) {
        //        TODO
        return null;
    }

    public Employee updateEmployee(Integer id, EmployeeDTO employee) {
        //        TODO
        return null;
    }

    public void deleteEmployee(Integer id) {
        //        TODO
        return;
    }
}
