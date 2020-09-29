package com.tony.automationserverweb.auth;

import com.tony.automationserverweb.dao.AccountRepositoryImpl;
import com.tony.automationserverweb.dao.AccountSessionRepositoryImpl;
import com.tony.automationserverweb.dao.DevAccountRepositoryImpl;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.AccountSession;
import com.tony.automationserverweb.model.DevAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiAuthenticationServiceImpl implements ApiAuthenticationService {

    @Autowired
    private AccountSessionRepositoryImpl accountSessionRepository;

    @Autowired
    private AccountRepositoryImpl accountRepository;
    
    @Autowired
    private DevAccountRepositoryImpl devAccountRepository;

    @Override
    public Account findAccountByToken(String token) {
        AccountSession session = accountSessionRepository.findOneByToken(token);
       
        if(session == null)
            return null;
        if(!"A".equals(session.getAccType()))
            return null;
        return accountRepository.findOneById(session.getAccountId());
    }

    @Override
    public DevAccount findDevAccountByToken(String token) {
        AccountSession session = accountSessionRepository.findOneByToken(token);
        if (session == null)
            return null;
        if (!"D".equals(session.getAccType()))
            return null;
        return devAccountRepository.findOneById(session.getAccountId());
    }

    @Override
    public void logout(Account account) {
       

    }

    @Override
    public void logout(DevAccount devAccount) {

    }
    
    
}
