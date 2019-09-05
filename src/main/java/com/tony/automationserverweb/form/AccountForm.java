package com.tony.automationserverweb.form;

// import java.util.ArrayList;
import java.util.HashMap;
// import java.util.HashSet;
// import java.util.List;
import java.util.Map;
// import java.util.Set;
// import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tony.automationserverweb.model.Account;
// import com.tony.automationserverweb.model.Device;

@JsonIgnoreProperties({"validated", "errors"})
public class AccountForm implements Form<Account> {

    private String userNickname;
    private String userEmail;
    private String userPassword;
    private String userPassword2;

    private boolean validated;
    private HashMap<String, String> errors;

    public AccountForm() {
        validated = false;
        errors = new HashMap<>();
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    @Override
    public boolean hasErrors() {
        getErrors();
        return errors.size() != 0;
    }

    @Override
    public Map<String, String> getErrors() {
        validate();
        return errors;
    }

    @Override
    public void validate() {
        if(validated)
            return;
        validated = true;
        String emailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
     
        if(userEmail == null)
            errors.put("userEmail", "Email is required");
        else if(!userEmail.matches(emailRegex))
            errors.put("userEmail", "Wrong email format");
        
        if(userPassword == null)
            errors.put("userPassword", "Password is required");
        else if (userPassword.length() < 8)
            errors.put("userPassword", "Minimum password length is 8");

        if (userNickname == null)
            errors.put("userNickname", "Nickname is required");
        else if (userNickname.length() < 3)
            errors.put("userPassword", "Minimum nickname length is 3");
    }

    public Account fill(){
        Account account = new Account();
        account.setEmail(userEmail);
        account.setPasswordHash(userPassword);
        account.setNickname(userNickname);
        return account;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPassword2() {
        return userPassword2;
    }

    public void setUserPassword2(String userPassword2) {
        this.userPassword2 = userPassword2;
    }
}
