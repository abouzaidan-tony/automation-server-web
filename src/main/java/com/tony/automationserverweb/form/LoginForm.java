package com.tony.automationserverweb.form;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.util.StringUtils;

@JsonIgnoreProperties({ "validated", "errors" })
public class LoginForm implements Form<Void> {

    public String email;
    public String password;

    private boolean validated;
    private HashMap<String, String> errors;

    public LoginForm() {
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

        if (StringUtils.isEmpty(email))
            errors.put("email", "Empty Email");

        if (StringUtils.isEmpty(password))
            errors.put("password", "Empty Password");
    }

    public Void fill(){
        return null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
