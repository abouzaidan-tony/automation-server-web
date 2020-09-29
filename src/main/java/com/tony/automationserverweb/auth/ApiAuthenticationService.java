package com.tony.automationserverweb.auth;


import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.DevAccount;

public interface ApiAuthenticationService {
    
    Account findAccountByToken(String token);

    DevAccount findDevAccountByToken(String token);

    void logout(Account account);

    void logout(DevAccount devAccount);
}
