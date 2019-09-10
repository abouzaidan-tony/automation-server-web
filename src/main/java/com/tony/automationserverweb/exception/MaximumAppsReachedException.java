package com.tony.automationserverweb.exception;

public class MaximumAppsReachedException extends ApplicationException {
    private static final long serialVersionUID = 1L;

    public MaximumAppsReachedException() {
        super("Maximum applications reached");
    }
}