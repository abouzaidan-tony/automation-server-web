package com.tony.automationserverweb.model;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class AuthUser {
    
    private Long id;
    private String password;
    private List<GrantedAuthority> authorities;
    private String applicationToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public String getApplicationToken() {
        return applicationToken;
    }

    public void setApplicationToken(String applicationToken) {
        this.applicationToken = applicationToken;
    }

    public AuthUser(Long id, String password, List<GrantedAuthority> authorities) {
        this.id = id;
        this.password = password;
        this.authorities = authorities;
    }

    public AuthUser(Long id, String password, List<GrantedAuthority> authorities, String applicationToken) {
        this.id = id;
        this.password = password;
        this.authorities = authorities;
        this.applicationToken = applicationToken;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
