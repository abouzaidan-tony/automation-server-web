package com.tony.automationserverweb.exception;

public class InvalidApplicationException extends ApplicationException {
    private static final long serialVersionUID = 1L;

    public InvalidApplicationException() {
        super("Invalid application");
    }
}