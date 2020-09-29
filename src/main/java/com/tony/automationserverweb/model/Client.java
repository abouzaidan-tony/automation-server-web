package com.tony.automationserverweb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "accountId", "account", "application" })
public abstract class Client {

    private long id;

    private String key;

    private boolean connected;

    private Account account;

    private Application application;

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

    public Long getAppId(){
        if(application == null)
            return null;
        return application.getId();
    }

    public String getAppToken() {
        if (application == null)
            return null;
        return application.getToken();
    }

    public String getAppName(){
        if(application == null)
            return null;
        return application.getName();
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

    public void setApplication(Application application){
        this.application = application;
    }

    public Application getApplication(){
        return application;
    }
}