package com.tony.automationserverweb.exception;

public abstract class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApplicationException(String msg) {
        super(msg);
    }
}