package com.tony.automationserverweb.exception;

public class EmailAlreadyExistsException extends ApplicationException {
    private static final long serialVersionUID = 1L;

    public EmailAlreadyExistsException() {
        super("Email Already exists");
    }
}