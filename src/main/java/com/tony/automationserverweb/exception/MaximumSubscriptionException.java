package com.tony.automationserverweb.exception;

public class MaximumSubscriptionException extends ApplicationException {
    private static final long serialVersionUID = 1L;

    public MaximumSubscriptionException() {
        super("Maximum applications subscription reached");
    }
}