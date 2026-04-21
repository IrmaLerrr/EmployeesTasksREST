package org.irmalerrr.employeeservice.exceptions;

public class EmployeeNotFoundException extends ElementNotFoundException {

    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id: " + id);
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
