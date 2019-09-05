package com.tony.automationserverweb.exception;

public class ResourceNotFoundException extends ApplicationException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException() {
        super("Resource not found");
    }
}