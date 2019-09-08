package com.tony.automationserverweb.form;

import java.util.HashMap;
import java.util.Map;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"validated", "errors"})
public class VerificationForm implements Form<String> {

    private String userCode;

    private boolean validated;
    private HashMap<String, String> errors;

    public VerificationForm() {
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
        if(validated)
            return;
        validated = true;

        if(userCode == null)
            errors.put("userCode", "Code is required");
    }

    public String fill(){
        return userCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
