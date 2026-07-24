package org.irmalerrr.employeeservice.exceptions;

import java.util.NoSuchElementException;

public class ElementNotFoundException extends NoSuchElementException {
    public ElementNotFoundException(String message) {
        super(message);
    }
}

