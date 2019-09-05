package com.tony.automationserverweb.exception;

public class MaximumDevicesReachedException extends ApplicationException {
    private static final long serialVersionUID = 1L;

    public MaximumDevicesReachedException() {
        super("Maximum devices reached");
    }
}