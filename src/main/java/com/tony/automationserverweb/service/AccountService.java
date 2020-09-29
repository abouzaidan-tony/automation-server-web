package com.tony.automationserverweb.service;

import com.tony.automationserverweb.dao.AccountRepositoryImpl;
import com.tony.automationserverweb.dao.ApplicationRepositoryImpl;
import com.tony.automationserverweb.dao.DeviceRepositoryImpl;
import com.tony.automationserverweb.dao.UserRepositoryImpl;
import com.tony.automationserverweb.exception.DuplicateDeviceKeyException;
import com.tony.automationserverweb.exception.EmailAlreadyExistsException;
import com.tony.automationserverweb.exception.InvalidApplicationException;
import com.tony.automationserverweb.exception.MaximumDevicesReachedException;
import com.tony.automationserverweb.exception.MaximumSubscriptionException;
import com.tony.automationserverweb.exception.ResourceNotFoundException;
import com.tony.automationserverweb.helper.Helper;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.Application;
import com.tony.automationserverweb.model.Device;
import com.tony.automationserverweb.model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private static Logger logger = LogManager.getLogger(AccountService.class);

    @Autowired
    private AccountRepositoryImpl accountRepositoryImpl;

    @Autowired
    private DeviceRepositoryImpl deviceRepositoryImpl;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private ApplicationRepositoryImpl applicationRepositoryImpl;

    @Autowired
    private MailService mailService;

    public Account createAccount(Account account) {
        account.setPasswordHash(Helper.Encode(account.getPasswordHash()));
        account.setToken(accountRepositoryImpl.generateUniqueToken());
        Integer count = accountRepositoryImpl.getCountUsersByEmail(account.getEmail());
        if (count != 0)
            throw new EmailAlreadyExistsException();
        //sendAccountVerification(account, false);
        account = accountRepositoryImpl.insert(account);

        return account;
    }

    public void sendAccountVerification(Account account, boolean save) {
        if (account == null)
            return;
        String otp = Helper.generateOTP();
        account.setOtp(otp);
        logger.debug("OTP : " + otp);
        try {
            mailService.sendMail(account.getEmail(), "Account Verification",
                    "Please use this code : " + otp + " to verify your account\n\nThank you!");
        } catch (Exception ex) {
            logger.error("Error", ex);
        }

        if (save)
            accountRepositoryImpl.update(account);
    }

    public boolean verifyAccount(Account account, String otp) {
        if (!account.getOtp().equals(otp))
            return false;
        account.setOtp(null);
        accountRepositoryImpl.update(account);
        return true;
    }

    public User addUser(Account account, User user) {
        if (account.getUsers().size() > 3)
            throw new MaximumDevicesReachedException();
        boolean duplicate = false;
        for (User var : account.getUsers()) {
            if (var.getKey().equals(user.getKey())) {
                duplicate = true;
                break;
            }
        }
        if (duplicate)
            throw new DuplicateDeviceKeyException(user.getKey());
        Application app;
        if (user.getAppToken() != null)
            app = Helper.getAppFromList(account.getSubscriptions(), user.getAppToken());
        else
            app = Helper.getAppFromList(account.getSubscriptions(), user.getAppId());
            
        if (app == null)
            throw new InvalidApplicationException();
        user.setApplication(app);
        user.setAccount(account);
        account.getUsers().add(user);
        userRepositoryImpl.insert(user);
        return user;
    }

    public void removeUser(Account account, User device) {
        boolean duplicate = false;
        for (User var : account.getUsers()) {
            if (var.getKey().equals(device.getKey())) {
                duplicate = true;
                device = var;
                break;
            }
        }
        if (!duplicate)
            throw new ResourceNotFoundException();
        device.setAccount(account);
        account.getUsers().remove(device);
        userRepositoryImpl.delete(device);
    }

    public Device addDevice(Account account, Device device) {
        if (account.getDevices().size() > 3)
            throw new MaximumDevicesReachedException();
        boolean duplicate = false;
        for (Device var : account.getDevices()) {
            if (var.getKey().equals(device.getKey())) {
                duplicate = true;
                break;
            }
        }
        if (duplicate)
            throw new DuplicateDeviceKeyException(device.getKey());

        Application app;
        if (device.getAppToken() != null)
            app = Helper.getAppFromList(account.getSubscriptions(), device.getAppToken());
        else
            app = Helper.getAppFromList(account.getSubscriptions(), device.getAppId());

        if (app == null)
            throw new InvalidApplicationException();

        device.setApplication(app);
        device.setAccount(account);
        account.getDevices().add(device);
        deviceRepositoryImpl.insert(device);
        return device;
    }

    public void removeDevice(Account account, Device device) {
        boolean duplicate = false;
        for (Device var : account.getDevices()) {
            if (var.getKey().equals(device.getKey())) {
                duplicate = true;
                device = var;
                break;
            }
        }
        if (!duplicate)
            throw new ResourceNotFoundException();
        device.setAccount(account);
        account.getDevices().remove(device);
        deviceRepositoryImpl.delete(device);
    }

    public AccountRepositoryImpl getAccountRepositoryImpl() {
        return accountRepositoryImpl;
    }

    public Application subscribe(Account account, Application application) {
        if (account.getSubscriptions().size() >= 4)
            throw new MaximumSubscriptionException();
        application = applicationRepositoryImpl.findOneByToken(application.getToken());
        if (application == null)
            return null;
        if (account.getSubscriptions().contains(application))
            return null;
        account.getSubscriptions().add(application);
        accountRepositoryImpl.subscribe(account, application);
        return application;
    }

    public Application unsubscribe(Account account, Application application) {
        application = applicationRepositoryImpl.findOneByToken(application.getToken());
        if (application == null)
            return null;
        if (!account.getSubscriptions().contains(application))
            return null;
        account.getSubscriptions().remove(application);
        accountRepositoryImpl.unsubscribe(account, application);
        return application;
    }

    public Account getAccountDetailsForApplication(long id, String appToken) {
        Account account = accountRepositoryImpl.getAccountByIdForApplication(id, appToken);

        if (account == null)
            throw new EmptyResultDataAccessException("Account not found", 1);
        return account;
    }
}