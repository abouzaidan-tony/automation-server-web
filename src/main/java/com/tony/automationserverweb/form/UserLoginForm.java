package com.tony.automationserverweb.form;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.util.StringUtils;

@JsonIgnoreProperties({ "validated", "errors" })
public class UserLoginForm implements Form<Void> {

    public String applicationToken;
    public String email;
    public String password;

    private boolean validated;
    private HashMap<String, String> errors;

    public UserLoginForm() {
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

        if (StringUtils.isEmpty(applicationToken))
            errors.put("applicationToken", "Application Token");

        if (StringUtils.isEmpty(email))
            errors.put("email", "Empty Email");

        if (StringUtils.isEmpty(password))
            errors.put("password", "Empty Password");
    }

    public Void fill(){
        return null;
    }
}
