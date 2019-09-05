package com.tony.automationserverweb.model;

public class Device extends Client {

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj instanceof Device)
            return ((Device) obj).getId() == this.getId();
        if(obj instanceof String)
            return ((String)obj).equals(this.getKey());
        if(obj instanceof Long)
            return ((Long) obj).equals(this.getId());
        return false;
    }
}