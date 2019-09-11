package com.tony.automationserverweb.form;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tony.automationserverweb.model.DevAccount;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@JsonIgnoreProperties({ "validated", "errors" })
public class DevAccountForm implements Form<DevAccount> {

    private String userEmail;
    private String userPassword;
    private String userPassword2;
    private String userInvoice;

    private boolean validated;
    private HashMap<String, String> errors;

    public DevAccountForm() {
        validated = false;
        errors = new HashMap<>();
    }

    public String getUserInvoice() {
        return userInvoice;
    }

    public void setUserInvoice(String userInvoice) {
        this.userInvoice = userInvoice;
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

        if (userInvoice == null)
            userInvoice = "";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        String fooResourceUrl = "https://api.assetstore.unity3d.com/publisher/v1/invoice/verify.json?key=AOYGTWPa1ICM7Xvl6VEV0K0cYOg&invoice="+userInvoice;
        ResponseEntity<String> response = null;
        try{
            response = restTemplate.exchange(fooResourceUrl, HttpMethod.GET, entity, String.class);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        
        boolean isWrong = true;
        do{
            if(response == null)
                break;

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
        account.setUnityInvoice(userInvoice);
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
