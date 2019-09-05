package com.tony.automationserverweb.controller;

import com.tony.automationserverweb.exception.ApplicationException;
import com.tony.automationserverweb.form.DeviceForm;
import com.tony.automationserverweb.form.UserForm;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.Device;
import com.tony.automationserverweb.model.Response;
import com.tony.automationserverweb.model.User;
import com.tony.automationserverweb.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/device/add")
    public @ResponseBody ResponseEntity<Object> addDevice(@RequestBody DeviceForm deviceForm){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account u = accountService.getAccountRepositoryImpl().findOneById(userId);
        if(deviceForm.hasErrors())
            return new ResponseEntity<>(new Response(false, deviceForm.getErrors()), HttpStatus.OK);
        Device d = deviceForm.fill();

        accountService.addDevice(u, d);

        return new ResponseEntity<>(new Response(true, d), HttpStatus.OK);
        
    }

    @PostMapping("/device/remove")
    public @ResponseBody Object removeDevice(@RequestBody DeviceForm deviceForm) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account u = accountService.getAccountRepositoryImpl().findOneById(userId);
        if (deviceForm.hasErrors())
            return new ResponseEntity<>(new Response(false, deviceForm.getErrors()), HttpStatus.OK);
        Device d = deviceForm.fill();

        accountService.removeDevice(u, d);

        return new ResponseEntity<>(new Response(true, null), HttpStatus.OK);
    }

    @PostMapping("/user/add")
    public @ResponseBody ResponseEntity<Object> addUser(@RequestBody UserForm deviceForm) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account u = accountService.getAccountRepositoryImpl().findOneById(userId);
        if (deviceForm.hasErrors())
            return new ResponseEntity<>(new Response(false, deviceForm.getErrors()), HttpStatus.OK);
        User d = deviceForm.fill();

        accountService.addUser(u, d);

        return new ResponseEntity<>(new Response(true, d), HttpStatus.OK);

    }

    @PostMapping("/user/remove")
    public @ResponseBody Object removeUser(@RequestBody UserForm deviceForm) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account u = accountService.getAccountRepositoryImpl().findOneById(userId);
        if (deviceForm.hasErrors())
            return new ResponseEntity<>(new Response(false, deviceForm.getErrors()), HttpStatus.OK);
        User d = deviceForm.fill();

        accountService.removeUser(u, d);

        return new ResponseEntity<>(new Response(true, null), HttpStatus.OK);
    }


    @ExceptionHandler
    public @ResponseBody ResponseEntity<Response> handleErrors(Exception ex){
        ex.printStackTrace();
        return new ResponseEntity<>(new Response(false, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    public @ResponseBody ResponseEntity<Response> handleAppErrors(Exception ex) {
        return new ResponseEntity<>(new Response(false, ex.getMessage()), HttpStatus.OK);
    }

    @GetMapping
    public String userMainPage(ModelMap model){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account u = accountService.getAccountRepositoryImpl().findOneById(userId);
        model.addAttribute("account", u);
        model.addAttribute("devices", u.getDevices());
        model.addAttribute("users", u.getUsers());
        return "account";
    }
}