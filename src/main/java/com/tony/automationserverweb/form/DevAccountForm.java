package com.tony.automationserverweb.form;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tony.automationserverweb.model.DevAccount;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@JsonIgnoreProperties({ "validated", "errors" })
public class DevAccountForm implements Form<DevAccount> {

    private String userEmail;
    private String userPassword;
    private String userPassword2;
    private String unityInvoice;

    private boolean validated;
    private HashMap<String, String> errors;

    public DevAccountForm() {
        validated = false;
        errors = new HashMap<>();
    }

    public String getUnityInvoice() {
        return unityInvoice;
    }

    public void setUnityInvoice(String unityInvoice) {
        this.unityInvoice = unityInvoice;
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

        if (unityInvoice == null)
            unityInvoice = "";

        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://api.assetstore.unity3d.com/publisher/v1/invoice/verify.json?key=?AOYGTWPa1ICM7Xvl6VEV0K0cYOg&invoice="+unityInvoice;
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
        
        boolean isWrong = true;
        do{
            if(!response.getStatusCode().equals(HttpStatus.OK))
                break;

            JSONObject obj = new JSONObject(response.getBody());
            if(!obj.has("invoices"))
                break;
            isWrong = false;

            JSONArray invoices = obj.getJSONArray("invoices");
            if(invoices.length() == 0)
                errors.put("unityInvoice", "Invalid unity invoice");
            else if(invoices.length() != 1)
                errors.put("unityInvoice", "Please submit 1 invoice number");

        }while(false);
       
        if(isWrong)
            errors.put("unityInvoice", "Error occured");
    }

    public DevAccount fill(){
        DevAccount account = new DevAccount();
        account.setEmail(userEmail);
        account.setPasswordHash(userPassword);
        account.setUnityInvoice(unityInvoice);
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
