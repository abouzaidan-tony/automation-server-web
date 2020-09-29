package com.tony.automationserverweb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Response {
    public boolean success;

    @JsonInclude(Include.NON_NULL)
    public Object data;

    public Response(boolean success, Object data){
        this.success = success;
        this.data = data;
    }
}