package com.tony.automationserverweb.service;

import com.tony.automationserverweb.dao.ApplicationRepositoryImpl;
import com.tony.automationserverweb.dao.DevAccountRepositoryImpl;
import com.tony.automationserverweb.exception.EmailAlreadyExistsException;
import com.tony.automationserverweb.exception.MaximumAppsReachedException;
import com.tony.automationserverweb.exception.ResourceNotFoundException;
import com.tony.automationserverweb.helper.Helper;
import com.tony.automationserverweb.model.Application;
import com.tony.automationserverweb.model.DevAccount;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DevAccountService {

    private static Logger logger = LogManager.getLogger(DevAccountService.class);

    @Autowired
    private DevAccountRepositoryImpl devAccountRepositoryImpl;

    @Autowired
    private ApplicationRepositoryImpl applicationRepositoryImpl;

    // @Autowired
    // private MailService mailService;

    public DevAccount createAccount(DevAccount account){
        account.setPasswordHash(Helper.Encode(account.getPasswordHash()));
        Integer count = devAccountRepositoryImpl.getCountAccountsByEmail(account.getEmail());
        account.setAnswer1(Helper.MD5(account.getAnswer1()));
        account.setAnswer2(Helper.MD5(account.getAnswer2()));
        //if(account.getUnityInvoice() != null && account.getUnityInvoice().length() != 0)
        account.setVerified(true);
        if(count != 0)
            throw new EmailAlreadyExistsException();
        //sendAccountVerification(account, false);
        account = devAccountRepositoryImpl.insert(account);

        return account;
    }

    public void sendAccountVerification(DevAccount account, boolean save){
        if(account == null)
            return;
        String otp = Helper.generateOTP();
        account.setOtp(otp);
        logger.debug("OTP : " + otp);
        try{
            //mailService.sendMail(account.getEmail(), "Developer Account Verification", "Hi,\n\nPlease use this code : " + otp + " to verify your account\n\n\nThank you!");
        }catch(Exception ex){
            logger.error("Error", ex);
        }

        if(save)
            devAccountRepositoryImpl.update(account);
    }

    public boolean verifyAccount(DevAccount account, String otp)
    {
        if(!account.getOtp().equals(otp))
            return false;
        account.setOtp(null);
        account.setVerified(true);
        devAccountRepositoryImpl.update(account);
        return true;
    }

    public void addApplication(DevAccount account, Application app) {
        if (account.getApplications().size() >= 2)
            throw new MaximumAppsReachedException();
        
        String token = applicationRepositoryImpl.generateUniqueToken();
        app.setToken(token);

        app.setAccount(account);
        account.getApplications().add(app);
        applicationRepositoryImpl.insert(app);
    }

    public void removeApplication(DevAccount account, Application app) {
        boolean duplicate = false;
        for (Application var : account.getApplications()){
            if (app.getToken().equals(var.getToken())) {
                duplicate = true;
                app = var;
                break;
            }
        }

        if (!duplicate)
            throw new ResourceNotFoundException();
        app.setAccount(account);
        account.getApplications().remove(app);
        applicationRepositoryImpl.delete(app);
    }

    public DevAccountRepositoryImpl getDevAccountRepositoryImpl() {
        return devAccountRepositoryImpl;
    }

    public void fillOnlineStatuses(DevAccount account){
        for (Application var : account.getApplications()) {
            var.setOnlineDevices(applicationRepositoryImpl.getOnlineDevices(var));
            var.setOnlineUsers(applicationRepositoryImpl.getOnlineUsers(var));
            var.setTotalSessions(var.getOnlineDevices() + var.getOnlineUsers());
            var.setTotalSubscriptions(applicationRepositoryImpl.getTotalSubscriptions(var));
        }
    }
}