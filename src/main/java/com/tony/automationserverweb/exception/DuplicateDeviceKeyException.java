package com.tony.automationserverweb.exception;

public class DuplicateDeviceKeyException extends ApplicationException {
    private static final long serialVersionUID = 1L;

    public DuplicateDeviceKeyException(String key) {
        super(key + " device key already in use");
    }
}