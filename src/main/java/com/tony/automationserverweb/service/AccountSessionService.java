package com.tony.automationserverweb.service;

import java.util.Date;

import com.tony.automationserverweb.dao.AccountSessionRepositoryImpl;
import com.tony.automationserverweb.helper.Helper;
import com.tony.automationserverweb.model.AccountSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountSessionService {

    private static Logger logger = LogManager.getLogger(AccountSessionService.class);

    @Autowired
    private AccountSessionRepositoryImpl accountSessionRepository;

    public AccountSession createAccountSession(Long accountId, String accountType, String appToken) {

        AccountSession accountSession = new AccountSession();
        accountSession.setAccountId(accountId);
        accountSession.setApplicationToken(appToken);
        accountSession.setToken(accountSessionRepository.generateUniqueToken(accountType));
        accountSession.setAccType(accountType);
        accountSession.setExpiryDate(Helper.addDaysToDate(new Date(), 2));
        accountSessionRepository.deleteByAccountId(accountId, accountType);
        accountSessionRepository.insert(accountSession);
        logger.debug("Account with type [" + accountType + "] Session created");
        return accountSession;
    }

}