package com.tony.automationserverweb.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "passwordHash", "otp"})
public class Account {

    private long id;

    private String email;

    private String nickname;

    private String token;

    private String passwordHash;

    private List<Device> devices;

    private List<User> users;

    private List<Application> subscriptions;

    private String otp;

    public Account() {
        devices = new ArrayList<>();
        users = new ArrayList<>();
        setSubscriptions(new ArrayList<>());
    }

    public List<Application> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Application> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getEmail() {
        return email;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}