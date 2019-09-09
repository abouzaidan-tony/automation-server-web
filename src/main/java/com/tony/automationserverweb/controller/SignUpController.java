package com.tony.automationserverweb.controller;

import javax.servlet.http.HttpServletRequest;

import com.tony.automationserverweb.exception.ApplicationException;
import com.tony.automationserverweb.form.AccountForm;
import com.tony.automationserverweb.form.VerificationForm;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.service.AccountService;

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
    private HttpServletRequest request;

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
            request.getSession().setAttribute("account", account);
            return "redirect:/signup/verify";

        }catch(ApplicationException ex){
            model.addAttribute("error", ex.getMessage());
        }
        
        return "signup";
    }
    

    @GetMapping("/verify")
    public ModelAndView verifyAccountPage(@ModelAttribute("verifyForm") VerificationForm verificationForm, Model model) {
        Account account = (Account) request.getSession().getAttribute("account");

        if (account == null)
            return new ModelAndView("redirect:/signup");

        return new ModelAndView("verifyAccount", "verifyForm", new VerificationForm());
    }

    @PostMapping("/verify")
    public String verifyAccountRequest(@ModelAttribute("verifyForm") VerificationForm verificationForm, Model model){

        Account account = (Account) request.getSession().getAttribute("account");

        if(account == null)
            return "redirect:/login";

        if (verificationForm.hasErrors()) {
            model.addAttribute("errors", verificationForm.getErrors());
            return "verifyAccount";
        }

        if(accountService.verifyAccount(account, verificationForm.fill()))
        {
            request.getSession().removeAttribute("account");
            return "redirect:/login";
        }
        model.addAttribute("error", "Wrong Code");
        return "verifyAccount";
    }
}