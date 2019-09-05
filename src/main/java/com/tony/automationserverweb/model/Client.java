package com.tony.automationserverweb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "accountId", "account" })
public abstract class Client {

    private long id;

    private String key;

    private boolean connected;

    private Account account;

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Account getAccount() {
        return account;
    }

    public Long getAccountId(){
        if(account == null)
            return null;
        return account.getId();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean getConnected() {
        return connected;
    }

    public Client() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}