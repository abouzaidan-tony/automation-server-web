package com.tony.automationserverweb.auth;

import java.util.ArrayList;
import java.util.List;
import com.tony.automationserverweb.dao.DevAccountRepositoryImpl;
import com.tony.automationserverweb.helper.Helper;
import com.tony.automationserverweb.model.DevAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class DevAccountAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    private DevAccountRepositoryImpl devAccountRepositoryImpl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        DevAccount user = devAccountRepositoryImpl.getDevAccountByEmail(email);

        if(user == null)
             throw new BadCredentialsException("Authentication failed for " + email);
    
        if(!Helper.EncodingMatches(password, user.getPasswordHash()))
            throw new BadCredentialsException("Authentication failed for " + email);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(user.getOtp() == null)
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_DEV"));
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getId(), user.getPasswordHash(), grantedAuthorities);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}