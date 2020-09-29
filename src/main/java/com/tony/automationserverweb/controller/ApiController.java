package com.tony.automationserverweb.controller;

import java.util.HashMap;
import java.util.Map;

import com.tony.automationserverweb.exception.ApplicationException;
import com.tony.automationserverweb.form.DeviceForm;
import com.tony.automationserverweb.form.UserForm;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.Application;
import com.tony.automationserverweb.model.ApplicationAccountTokenAuthentication;
import com.tony.automationserverweb.model.Device;
import com.tony.automationserverweb.model.Response;
import com.tony.automationserverweb.model.User;
import com.tony.automationserverweb.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApiController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/api/login")
    public @ResponseBody ResponseEntity<Object> login() {
        return new ResponseEntity<>(new Response(true, null), HttpStatus.OK);
    }

    @RequestMapping("/api/account/devices")
    public @ResponseBody ResponseEntity<Object> getUserDevices() {
        ApplicationAccountTokenAuthentication auth = (ApplicationAccountTokenAuthentication) SecurityContextHolder
                .getContext().getAuthentication();

        Account account = accountService.getAccountDetailsForApplication((Long) auth.getPrincipal(),
                auth.getApplicationToken());

        Map<String, Object> map = new HashMap<>();
        map.put("users", account.getUsers());
        map.put("account", account.getDevices());
        return new ResponseEntity<>(new Response(true, map), HttpStatus.OK);
    }

    @ExceptionHandler
    public @ResponseBody ResponseEntity<Response> handleErrors(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new Response(false, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    public @ResponseBody ResponseEntity<Response> handleAppErrors(Exception ex) {
        return new ResponseEntity<>(new Response(false, ex.getMessage()), HttpStatus.OK);
    }

    // @PostMapping("/api/dev/login")
    // public @ResponseBody ResponseEntity<Object> devLogin() {
    //     return new ResponseEntity<>(new Response(true, null), HttpStatus.OK);
    // }

    @PostMapping("/api/account/app/subscribe")
    public @ResponseBody ResponseEntity<Object> appSubscribe() {
        ApplicationAccountTokenAuthentication auth = (ApplicationAccountTokenAuthentication) SecurityContextHolder
                .getContext().getAuthentication();

        Long userId = (Long) auth.getPrincipal();
        Account u = accountService.getAccountRepositoryImpl().findOneById(userId);

        Application d = new Application();
        d.setToken(auth.getApplicationToken());

        d = accountService.subscribe(u, d);
        if (d == null)
            return new ResponseEntity<>(new Response(false, "Cannot subscribe"), HttpStatus.OK);

        return new ResponseEntity<>(new Response(true, d), HttpStatus.OK);

    }

    @PostMapping("/api/account/device/add")
    public @ResponseBody ResponseEntity<Object> addDevice(@RequestBody DeviceForm deviceForm) {
        ApplicationAccountTokenAuthentication auth = (ApplicationAccountTokenAuthentication) SecurityContextHolder
                .getContext().getAuthentication();

        Long userId = (Long) auth.getPrincipal();

        Account u = accountService.getAccountRepositoryImpl().findOneById(userId);
        deviceForm.setMandatoryAppId(false);
        if (deviceForm.hasErrors())
            return new ResponseEntity<>(new Response(false, deviceForm.getErrors()), HttpStatus.OK);
        Device d = deviceForm.fill();
        d.getApplication().setToken(auth.getApplicationToken());
        d = accountService.addDevice(u, d);

        return new ResponseEntity<>(new Response(true, d), HttpStatus.OK);

    }

    @PostMapping("/api/account/device/remove")
    public @ResponseBody Object removeDevice(@RequestBody DeviceForm deviceForm) {
        ApplicationAccountTokenAuthentication auth = (ApplicationAccountTokenAuthentication) SecurityContextHolder
                .getContext().getAuthentication();

        Long userId = (Long) auth.getPrincipal();

        Account u = accountService.getAccountRepositoryImpl().findOneById(userId);
        deviceForm.setMandatoryAppId(false);
        if (deviceForm.hasErrors())
            return new ResponseEntity<>(new Response(false, deviceForm.getErrors()), HttpStatus.OK);
        Device d = deviceForm.fill();
    
        accountService.removeDevice(u, d);

        return new ResponseEntity<>(new Response(true, null), HttpStatus.OK);
    }

    @PostMapping("/api/account/user/add")
    public @ResponseBody ResponseEntity<Object> addUser(@RequestBody UserForm deviceForm) {
        ApplicationAccountTokenAuthentication auth = (ApplicationAccountTokenAuthentication) SecurityContextHolder
                .getContext().getAuthentication();

        Long userId = (Long) auth.getPrincipal();

        Account u = accountService.getAccountRepositoryImpl().findOneById(userId);
        deviceForm.setMandatoryAppId(false);
        if (deviceForm.hasErrors())
            return new ResponseEntity<>(new Response(false, deviceForm.getErrors()), HttpStatus.OK);
        User d = deviceForm.fill();
        d.getApplication().setToken(auth.getApplicationToken());

        d = accountService.addUser(u, d);

        return new ResponseEntity<>(new Response(true, d), HttpStatus.OK);

    }

    @PostMapping("/api/account/user/remove")
    public @ResponseBody Object removeUser(@RequestBody UserForm deviceForm) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account u = accountService.getAccountRepositoryImpl().findOneById(userId);
        if (deviceForm.hasErrors())
            return new ResponseEntity<>(new Response(false, deviceForm.getErrors()), HttpStatus.OK);
        User d = deviceForm.fill();

        accountService.removeUser(u, d);

        return new ResponseEntity<>(new Response(true, null), HttpStatus.OK);
    }
}