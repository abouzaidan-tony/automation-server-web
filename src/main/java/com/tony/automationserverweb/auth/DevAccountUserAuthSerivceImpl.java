package com.tony.automationserverweb.auth;

import java.util.ArrayList;
import java.util.List;

import com.tony.automationserverweb.dao.AccountSessionRepositoryImpl;
import com.tony.automationserverweb.dao.DevAccountRepositoryImpl;
import com.tony.automationserverweb.model.AccountSession;
import com.tony.automationserverweb.model.AuthUser;
import com.tony.automationserverweb.model.DevAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("devAccountUserAuthSerivce")
public class DevAccountUserAuthSerivceImpl implements AuthUserService {

    @Autowired
    private DevAccountRepositoryImpl devAccountRepositoryImpl;

    @Autowired
    private AccountSessionRepositoryImpl accountSessionRepository;

    @Autowired
    private DevAccountRepositoryImpl devAccountRepository;
    
    @Override
    public AuthUser loadUserByUsername(String email) throws UsernameNotFoundException {
        DevAccount user = devAccountRepositoryImpl.getDevAccountByEmail(email);
        
        if (user == null)
            throw new BadCredentialsException("Authentication failed for " + email);
            
        return new AuthUser(user.getId(), user.getPasswordHash(), getGrantedAuthorities(user));
    }

    public static List<GrantedAuthority> getGrantedAuthorities(DevAccount user)
    {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user.getOtp() == null && user.isVerified())
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_DEV"));
        else if (user.getOtp() != null)
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_DEV_N"));
        return grantedAuthorities;
    }

    @Override
    public AuthUser loadUserByToken(String token) throws UsernameNotFoundException {
        AccountSession session = accountSessionRepository.findOneByToken(token);
        if (session == null)
            return null;
        if (!"D".equals(session.getAccType()))
            return null;
        DevAccount account =  devAccountRepository.findOneById(session.getAccountId());
        return new AuthUser(account.getId(), null, getGrantedAuthorities(account));
    }
    
}
