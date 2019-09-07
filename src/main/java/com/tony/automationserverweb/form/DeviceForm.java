package com.tony.automationserverweb.form;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tony.automationserverweb.model.Device;

@JsonIgnoreProperties({ "validated", "errors" })
public class DeviceForm implements Form<Device> {

    public String deviceKey;

    private boolean validated;
    private HashMap<String, String> errors;

    public DeviceForm() {
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

    public Device fill(){
        Device d = new Device();
        d.setKey(deviceKey);
        return d;
    }
}
