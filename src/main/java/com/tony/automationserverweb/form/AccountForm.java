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

    private Integer q1;
    private String answer1;

    private Integer q2;
    private String answer2;

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
        else if (!userPassword.equals(userPassword2))
            errors.put("userPassword", "Passwords does not match");

        if (userNickname == null)
            errors.put("userNickname", "Nickname is required");
        else if (userNickname.length() < 3)
            errors.put("userPassword", "Minimum nickname length is 3");

        if (q1 == null)
            errors.put("q1", "Question 1 not set");

        if (q2 == null)
            errors.put("q2", "Question 2 not set");

        if (answer1 == null || answer1.trim().length() < 3)
            errors.put("answer1", "Answer 1 not set");

        if (answer2 == null || answer2.trim().length() < 3)
            errors.put("answer2", "Answer 2 not set");

        if (q1 != null && q2 != null && q1.equals(q2))
            errors.put("q2", "Question 1 chosen same as question 2");
    }

    public Account fill(){
        Account account = new Account();
        account.setEmail(userEmail.toLowerCase());
        account.setPasswordHash(userPassword);
        account.setNickname(userNickname);
        account.setQ1(q1);
        account.setQ2(q2);
        account.setAnswer1(answer1);
        account.setAnswer2(answer2);
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

    public Integer getQ1() {
        return q1;
    }

    public void setQ1(Integer q1) {
        this.q1 = q1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public Integer getQ2() {
        return q2;
    }

    public void setQ2(Integer q2) {
        this.q2 = q2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }
}
