package com.tony.automationserverweb.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("passwordHash")
public class DevAccount {
    private long id;

    private String email;

    private String passwordHash;

    private String otp;

    private String unityInvoice;

    private List<Application> applicatons;

    public DevAccount() {
        applicatons = new ArrayList<>();
    }

    public String getUnityInvoice() {
        return unityInvoice;
    }

    public void setUnityInvoice(String unityInvoice) {
        this.unityInvoice = unityInvoice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public List<Application> getApplicatons() {
        return applicatons;
    }

    public void setApplicatons(List<Application> applicatons) {
        this.applicatons = applicatons;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof DevAccount)
            return ((User) obj).getId() == this.getId();
        if (obj instanceof Long)
            return ((Long) obj).equals(this.getId());
        return false;
    }
}