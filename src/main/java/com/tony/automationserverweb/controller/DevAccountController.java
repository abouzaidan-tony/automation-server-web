package com.tony.automationserverweb.controller;

import com.tony.automationserverweb.exception.ApplicationException;
import com.tony.automationserverweb.form.ApplicationForm;
import com.tony.automationserverweb.model.Application;
import com.tony.automationserverweb.model.DevAccount;
import com.tony.automationserverweb.model.Response;
import com.tony.automationserverweb.service.DevAccountService;

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
@RequestMapping("/dev/account")
public class DevAccountController {

    @Autowired
    private DevAccountService devAccountService;

    @PostMapping("/app/add")
    public @ResponseBody ResponseEntity<Object> addDevice(@RequestBody ApplicationForm appForm){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DevAccount u = devAccountService.getDevAccountRepositoryImpl().findOneById(userId);
        if(appForm.hasErrors())
            return new ResponseEntity<>(new Response(false, appForm.getErrors()), HttpStatus.OK);
        Application d = appForm.fill();

        devAccountService.addApplication(u, d);

        return new ResponseEntity<>(new Response(true, d), HttpStatus.OK);
        
    }

    @PostMapping("/app/remove")
    public @ResponseBody Object removeDevice(@RequestBody ApplicationForm appForm) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DevAccount u = devAccountService.getDevAccountRepositoryImpl().findOneById(userId);
        if (appForm.hasErrors())
            return new ResponseEntity<>(new Response(false, appForm.getErrors()), HttpStatus.OK);
        Application d = appForm.fill();

        devAccountService.removeApplication(u, d);

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
        DevAccount u = devAccountService.getDevAccountRepositoryImpl().findOneById(userId);
        model.addAttribute("account", u);
        model.addAttribute("apps", u.getApplications());
        return "dev_account";
    }
}