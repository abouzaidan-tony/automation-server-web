package com.tony.automationserverweb.form;

import java.util.Map;

public interface Form <T>{

    boolean hasErrors();
    Map<String, String> getErrors();
    void validate();
    T fill();
}