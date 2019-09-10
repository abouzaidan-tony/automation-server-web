package com.tony.automationserverweb.service;

import com.tony.automationserverweb.dao.AccountRepositoryImpl;
import com.tony.automationserverweb.dao.DeviceRepositoryImpl;
import com.tony.automationserverweb.dao.UserRepositoryImpl;
import com.tony.automationserverweb.exception.DuplicateDeviceKeyException;
import com.tony.automationserverweb.exception.EmailAlreadyExistsException;
import com.tony.automationserverweb.exception.MaximumDevicesReachedException;
import com.tony.automationserverweb.exception.ResourceNotFoundException;
import com.tony.automationserverweb.helper.Helper;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.Device;
import com.tony.automationserverweb.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepositoryImpl accountRepositoryImpl;

    @Autowired
    private DeviceRepositoryImpl deviceRepositoryImpl;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private MailService mailService;

    public Account createAccount(Account account){
        account.setPasswordHash(Helper.Encode(account.getPasswordHash()));
        account.setToken(accountRepositoryImpl.generateUniqueToken());
        Integer count = accountRepositoryImpl.getCountUsersByEmail(account.getEmail());
        sendAccountVerification(account, false);
        if(count != 0)
            throw new EmailAlreadyExistsException();
        account = accountRepositoryImpl.insert(account);

        return account;
    }

    public void sendAccountVerification(Account account, boolean save){
        if(account == null)
            return;
        String otp = Helper.generateOTP();
        account.setOtp(otp);
        try{
            mailService.sendMail(account.getEmail(), "Account Verification", "Please use this code : " + otp + " to verify your account\n\nThank you!");
        }catch(Exception ex){}

        if(save)
            accountRepositoryImpl.update(account);
    }

    public boolean verifyAccount(Account account, String otp)
    {
        if(!account.getOtp().equals(otp))
            return false;
        account.setOtp(null);
        accountRepositoryImpl.update(account);
        return true;
    }

    public void addUser(Account account, User device) {
        if (account.getUsers().size() > 3)
            throw new MaximumDevicesReachedException();
        boolean duplicate = false;
        for (User var : account.getUsers()) {
            if (var.getKey().equals(device.getKey())) {
                duplicate = true;
                break;
            }
        }
        if (duplicate)
            throw new DuplicateDeviceKeyException(device.getKey());
        device.setAccount(account);
        account.getUsers().add(device);
        userRepositoryImpl.insert(device);
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

    public void addDevice(Account account, Device device) {
        if(account.getDevices().size() > 3)
            throw new MaximumDevicesReachedException();
        boolean duplicate = false;
        for (Device var : account.getDevices()) {
            if(var.getKey().equals(device.getKey()))
            {
                duplicate = true;
                break;
            }
        }
        if(duplicate)
            throw new DuplicateDeviceKeyException(device.getKey());
        device.setAccount(account);
        account.getDevices().add(device);
        deviceRepositoryImpl.insert(device);
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

    /**
     * @return the userRepositoryImpl
     */
    public AccountRepositoryImpl getAccountRepositoryImpl() {
        return accountRepositoryImpl;
    }
}