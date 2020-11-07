package com.tony.automationserverweb.form;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tony.automationserverweb.model.DevAccount;


@JsonIgnoreProperties({ "validated", "errors" })
public class DevAccountForm implements Form<DevAccount> {

    private String userEmail;
    private String userPassword;
    private String userPassword2;
    private String userInvoice;

    private Integer q1;
    private String answer1;

    private Integer q2;
    private String answer2;


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

        if (q1 == null)
            errors.put("q1", "Question 1 not set");

        if (q2 == null)
            errors.put("q2", "Question 2 not set");
        
        if(answer1 == null || answer1.trim().length() < 3)
            errors.put("answer1", "Answer 1 not set");

        if (answer2 == null || answer2.trim().length() < 3)
            errors.put("answer2", "Answer 2 not set");

        if(q1 != null && q2 != null && q1.equals(q2))
            errors.put("q2", "Question 1 chosen same as question 2");

        // RestTemplate restTemplate = new RestTemplate();
        // HttpHeaders headers = new HttpHeaders();
        // headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        // headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        // HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        // headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        // String fooResourceUrl = "https://api.assetstore.unity3d.com/publisher/v1/invoice/verify.json?key=AOYGTWPa1ICM7Xvl6VEV0K0cYOg&invoice="+userInvoice;
        // ResponseEntity<String> response = null;
        // try{
        //     response = restTemplate.exchange(fooResourceUrl, HttpMethod.GET, entity, String.class);
        // }catch(Exception ex){
        //     ex.printStackTrace();
        // }
        
        
        // boolean isWrong = true;

        // do{

        //     if(userInvoice.length() == 0) {
        //         isWrong = false;
        //         break;
        //     }

        //     if(response == null)
        //         break;

            

        //     if(!response.getStatusCode().equals(HttpStatus.OK))
        //         break;

        //     JSONObject obj = new JSONObject(response.getBody());
        //     if(!obj.has("invoices"))
        //         break;
        //     isWrong = false;

        //     Object invoicesObj = obj.get("invoices");
        //     JSONArray invoices = null;
        //     if(invoicesObj instanceof JSONArray)
        //         invoices = (JSONArray) invoicesObj;
        //     else {
        //         errors.put("unityInvoice", "Invalid unity invoice");
        //         break;
        //     }

        //     if(invoices.length() == 0)
        //         errors.put("unityInvoice", "Invalid unity invoice");
        //     else if(invoices.length() != 1)
        //         errors.put("unityInvoice", "Please submit 1 invoice number");
        //     else {
        //         JSONObject invoice = invoices.getJSONObject(0);
        //         String invoicePackage = invoice.getString("package");
        //         if(!"ESP WiFi plugin".equals(invoicePackage)){
        //             errors.put("unityInvoice", "This invoice is for " + invoicePackage + " and not for ESP WiFi plugin");
        //         }
        //     }

        // }while(false);
       
        // if(isWrong)
        //     errors.put("unityInvoice", "Error occured");
    }

    public DevAccount fill(){
        DevAccount account = new DevAccount();
        account.setEmail(userEmail.toLowerCase());
        account.setPasswordHash(userPassword);
        account.setUnityInvoice(userInvoice);
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
