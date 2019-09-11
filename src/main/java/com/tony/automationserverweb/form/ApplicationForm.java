package com.tony.automationserverweb.form;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tony.automationserverweb.model.Application;

@JsonIgnoreProperties({ "validated", "errors" })
public class ApplicationForm implements Form<Application> {

    public String appToken;
    public String appName;

    private boolean validated;
    private HashMap<String, String> errors;

    public ApplicationForm() {
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

        if(appName == null && appToken == null)
            errors.put("error", "Missing Fields");

    }

    public Application fill(){
        Application d = new Application();
        d.setName(appName);
        d.setToken(appToken);
        return d;
    }
}
