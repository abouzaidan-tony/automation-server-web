package com.tony.automationserverweb.auth;

import com.tony.automationserverweb.model.AuthUser;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthUserService {
    AuthUser loadUserByUsername(String username) throws UsernameNotFoundException;
    AuthUser loadUserByToken(String token) throws UsernameNotFoundException;
}
