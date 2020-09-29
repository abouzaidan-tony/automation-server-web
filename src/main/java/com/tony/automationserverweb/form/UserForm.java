package com.tony.automationserverweb.form;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tony.automationserverweb.model.Application;
import com.tony.automationserverweb.model.User;

@JsonIgnoreProperties({ "validated", "errors", "mandatoryAppId" })
public class UserForm implements Form<User> {

    private boolean mandatoryAppId = true;

    public String deviceKey;
    public Long applicationId;

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
        d.setApplication(new Application());
        d.getApplication().setId(applicationId);
        d.setKey(deviceKey);
        return d;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public boolean isMandatoryAppId() {
        return mandatoryAppId;
    }

    public void setMandatoryAppId(boolean mandatoryAppId) {
        this.mandatoryAppId = mandatoryAppId;
    }
}
