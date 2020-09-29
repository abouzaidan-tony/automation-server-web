package com.tony.automationserverweb.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"passwordHash", "otp", "verified", "unityInvoice"})
public class DevAccount {
    private long id;

    private String email;

    private String passwordHash;

    private String otp;

    private String unityInvoice;

    private List<Application> applications;

    private boolean verified;

    private int q1;
    private int q2;

    private String answer1;
    private String answer2;

    public DevAccount() {
        setApplications(new ArrayList<>());
        verified = false;
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

    public int getQ1() {
        return q1;
    }

    public void setQ1(int q1) {
        this.q1 = q1;
    }

    public int getQ2() {
        return q2;
    }

    public void setQ2(int q2) {
        this.q2 = q2;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }
}