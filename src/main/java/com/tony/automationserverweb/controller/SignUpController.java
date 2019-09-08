package com.tony.automationserverweb.controller;

import com.tony.automationserverweb.exception.ApplicationException;
import com.tony.automationserverweb.form.AccountForm;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.service.AccountService;
import com.tony.automationserverweb.service.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MailService mailService;

    @GetMapping
    public ModelAndView signUpPage(){
        return new ModelAndView("signup", "userForm", new AccountForm());
    }

    @PostMapping()
    public String signUpSubmission(@ModelAttribute("userForm") AccountForm userForm, Model model){
        if(userForm.hasErrors())
        {
            userForm.setUserPassword(null);
            userForm.setUserPassword2(null);
            model.addAttribute("errors", userForm.getErrors());
            return "signup";
        }
        Account account = userForm.fill();
        userForm.setUserPassword(null);
        userForm.setUserPassword2(null);

        try{
            accountService.createAccount(account);
            mailService.sendMail(account.getEmail(), "Account Verification", "Please verify you account");
            return "login";

        }catch(ApplicationException ex){
            model.addAttribute("error", ex.getMessage());
        }
        
        return "signup";
       
    }
}