package com.tony.automationserverweb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("account")
public class Application {
    private Long id;
    private String name;
    private String token;
    private DevAccount account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DevAccount getAccount() {
        return account;
    }

    public Long getAccountId(){
        return account == null ? null : account.getId();
    }

    public void setAccount(DevAccount account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Application)
            return ((Application) obj).getId() == this.getId();
        if (obj instanceof Long)
            return ((Long) obj).equals(this.getId());
        return false;
    }
}