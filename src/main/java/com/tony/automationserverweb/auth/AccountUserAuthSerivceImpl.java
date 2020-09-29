package com.tony.automationserverweb.auth;

import java.util.ArrayList;
import java.util.List;

import com.tony.automationserverweb.dao.AccountRepositoryImpl;
import com.tony.automationserverweb.dao.AccountSessionRepositoryImpl;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.AccountSession;
import com.tony.automationserverweb.model.AuthUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("accountUserAuthSerivce")
public class AccountUserAuthSerivceImpl implements AuthUserService {

    @Autowired
    private AccountRepositoryImpl accountRepositoryImpl;

    @Autowired
    private AccountSessionRepositoryImpl accountSessionRepository;

    @Autowired
    private AccountRepositoryImpl accountRepository;

    @Override
    public AuthUser loadUserByUsername(String email) throws UsernameNotFoundException {
        Account user = accountRepositoryImpl.getUserByEmail(email);

        if (user == null)
            throw new BadCredentialsException("Authentication failed for " + email);

        return new AuthUser(user.getId(), user.getPasswordHash(), getGrantedAuthorities(user));
    }

    public static List<GrantedAuthority> getGrantedAuthorities(Account user) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user.getOtp() == null)
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        else
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_N"));

        return grantedAuthorities;
    }

    @Override
    public AuthUser loadUserByToken(String token) throws UsernameNotFoundException {
        AccountSession session = accountSessionRepository.findOneByToken(token);
        if (session == null)
            return null;
        if (!"A".equals(session.getAccType()))
            return null;
        Account account = accountRepository.findOneById(session.getAccountId());

        return new AuthUser(account.getId(), null, getGrantedAuthorities(account), session.getApplicationToken());
    }

}
