package com.tony.automationserverweb.service;

import com.tony.automationserverweb.dao.AccountRepositoryImpl;
import com.tony.automationserverweb.dao.DeviceRepositoryImpl;
import com.tony.automationserverweb.dao.UserRepositoryImpl;
import com.tony.automationserverweb.exception.DuplicateDeviceKeyException;
import com.tony.automationserverweb.exception.EmailAlreadyExistsException;
import com.tony.automationserverweb.exception.MaximumDevicesReachedException;
import com.tony.automationserverweb.exception.ResourceNotFoundException;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.Device;
import com.tony.automationserverweb.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        account.setPasswordHash(Encode(account.getPasswordHash()));
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
        String otp = generateOTP();
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

    public static String Encode(String pass){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        return encoder.encode(pass);
        // try{
        //     MessageDigest md = MessageDigest.getInstance("MD5");
        //     md.update(pass.getBytes());
        //     byte[] digest = md.digest();
        //     return DatatypeConverter.printHexBinary(digest).toUpperCase();
        // }catch(Exception ex){
        //     return null;
        // }
    }

    public static boolean EncodingMatches(String password, String encodedPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        return encoder.matches(password, encodedPassword);
    }

    private static final String ALPHA_NUMERIC_STRING = "0123456789";

    private static String generateOTP() {
        StringBuilder builder = new StringBuilder();
        int count = 5;
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


}