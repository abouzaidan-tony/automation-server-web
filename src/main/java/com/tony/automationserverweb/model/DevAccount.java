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

    private List<Application> applications;

    private boolean verified;

    public DevAccount() {
        setApplications(new ArrayList<>());
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof DevAccount)
            return ((DevAccount) obj).getId() == this.getId();
        if (obj instanceof Long)
            return ((Long) obj).equals(this.getId());
        return false;
    }
}