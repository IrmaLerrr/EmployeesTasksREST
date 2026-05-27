package org.irmalerrr.employeeservice.exceptions;

public class EmployeeNotFoundException extends ElementNotFoundException {

    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id: " + id);
    }

    public EmployeeNotFoundException() {
        super("Employee not found");
    }
    //todo - DONE - неиспользуемый код
}
