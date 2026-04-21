package org.irmalerrr.employeeservice.exceptions;

public class TaskNotFoundException extends ElementNotFoundException {

    public TaskNotFoundException(Long id) {
        super("Task not found with id: " + id);
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
