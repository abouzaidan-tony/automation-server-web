package com.tony.automationserverweb.model;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ApplicationAccountTokenAuthentication extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = 6915433807646734499L;

    private String applicationToken;

    public ApplicationAccountTokenAuthentication(Object principal, Object credentials,
            Collection<? extends GrantedAuthority> authorities, String applicationToken) {
        super(principal, credentials, authorities);
        this.applicationToken = applicationToken;
    }

    public ApplicationAccountTokenAuthentication(Object principal, Object credentials, String applicationToken) {
        super(principal, credentials);
        this.applicationToken = applicationToken;
    }

    public String getApplicationToken() {
        return applicationToken;
    }

    public void setApplicationToken(String applicationToken) {
        this.applicationToken = applicationToken;
    }

}
