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

    public Account createAccount(Account account){
        account.setPasswordHash(Encode(account.getPasswordHash()));
        account.setToken(accountRepositoryImpl.generateUniqueToken());
        Integer count = accountRepositoryImpl.getCountUsersByEmail(account.getEmail());
        if(count != 0)
            throw new EmailAlreadyExistsException();
        account = accountRepositoryImpl.insert(account);
        return account;
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


}