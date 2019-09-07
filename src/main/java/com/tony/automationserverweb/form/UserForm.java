package com.tony.automationserverweb.form;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tony.automationserverweb.model.User;

@JsonIgnoreProperties({ "validated", "errors" })
public class UserForm implements Form<User> {

    public String deviceKey;

    private boolean validated;
    private HashMap<String, String> errors;

    public UserForm() {
        validated = false;
        errors = new HashMap<>();
    }

    @Override
    public boolean hasErrors() {
        getErrors();
        return errors.size() != 0;
    }

    @Override
    public Map<String, String> getErrors() {
        validate();
        return errors;
    }

    @Override
    public void validate() {
        if (validated)
            return;
        validated = true;

        if (deviceKey != null && deviceKey.length() != 5)
            errors.put("deviceKey", "Device Key must be 5 characters");
    }

    public User fill(){
        User d = new User();
        d.setKey(deviceKey);
        return d;
    }
}
