package com.tony.automationserverweb.controller;

import javax.servlet.http.HttpServletRequest;

import com.tony.automationserverweb.dao.QuestionRepositoryImpl;
import com.tony.automationserverweb.exception.ApplicationException;
import com.tony.automationserverweb.form.AccountForm;
import com.tony.automationserverweb.form.DevAccountForm;
import com.tony.automationserverweb.form.VerificationForm;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.DevAccount;
import com.tony.automationserverweb.service.AccountService;
import com.tony.automationserverweb.service.DevAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SignUpController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private DevAccountService devAccountService;

    @Autowired
    private QuestionRepositoryImpl questionRepository;

    // @Autowired
    // private MailService mailService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/signup")
    public ModelAndView signUpPage(HttpServletRequest request, Model model){
        model.addAttribute("questions", questionRepository.getAll());
        return new ModelAndView("signup", "userForm", new AccountForm());
    }

    @GetMapping("/dev/signup")
    public ModelAndView devSignUpPage(HttpServletRequest request, Model model) {
        model.addAttribute("questions", questionRepository.getAll());
        return new ModelAndView("dev_signup", "devForm", new DevAccountForm());
    }

    @PostMapping("/signup")
    public String signUpSubmission(@ModelAttribute("userForm") AccountForm userForm, Model model){
        if(userForm.hasErrors())
        {
            userForm.setUserPassword(null);
            userForm.setUserPassword2(null);
            model.addAttribute("errors", userForm.getErrors());
            model.addAttribute("questions", questionRepository.getAll());
            return "signup";
        }
        Account account = userForm.fill();
        userForm.setUserPassword(null);
        userForm.setUserPassword2(null);

        try{
            account = accountService.createAccount(account);
            request.getSession().setAttribute("account", account);
            if (account.getOtp() != null)
                return "redirect:/signup/verify";
            else
                return "redirect:/login";

        }catch(ApplicationException ex){
            model.addAttribute("error", ex.getMessage());
        }
        
        return "signup";
    }

    @PostMapping("/dev/signup")
    public String devSignUpSubmission(@ModelAttribute("devForm") DevAccountForm userForm, Model model) {
        if (userForm.hasErrors()) {
            userForm.setUserPassword(null);
            userForm.setUserPassword2(null);
            model.addAttribute("errors", userForm.getErrors());
            model.addAttribute("questions", questionRepository.getAll());
            return "dev_signup";
        }
        DevAccount account = userForm.fill();
        userForm.setUserPassword(null);
        userForm.setUserPassword2(null);

        try {
            account = devAccountService.createAccount(account);
            request.getSession().setAttribute("account", account);
            if (account.getOtp() != null)
                return "redirect:/dev/signup/verify";
            else
                return "redirect:/dev/login";

        } catch (ApplicationException ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "dev_signup";
    }
    

    @GetMapping({ "/signup/verify", "/dev/signup/verify" })
    public ModelAndView verifyAccountPage(@ModelAttribute("verifyForm") VerificationForm verificationForm, Model model) {
        Object account = request.getSession().getAttribute("account");

        String url = null;
        String currentUrl = request.getRequestURI();
        if(currentUrl.equals("/verify"))
            url = "redirect:/signup";
        else
            url = "redirect:/dev/signup";

        if (account == null)
            return new ModelAndView(url);

        // mailService.sendMail(((Account)account).getEmail(), "Developer Account Verification",
        //         "Please use this code : " + ((Account) account).getOtp() + " to verify your account\n\nThank you!");

        return new ModelAndView("verifyAccount", "verifyForm", new VerificationForm());
    }

    @PostMapping({"/signup/verify", "/dev/signup/verify"})
    public String verifyAccountRequest(@ModelAttribute("verifyForm") VerificationForm verificationForm, Model model){

        Object account = request.getSession().getAttribute("account");


        String url = null;
        String currentUrl = request.getRequestURI();
        if (currentUrl.equals("/signup/verify"))
            url = "redirect:/login";
        else
            url = "redirect:/dev/login";

        if(account == null)
            return url;

        if (verificationForm.hasErrors()) {
            model.addAttribute("errors", verificationForm.getErrors());
            return "verifyAccount";
        }

        if(account instanceof Account) {
            if(accountService.verifyAccount((Account)account, verificationForm.fill()))
            {
                request.getSession().removeAttribute("account");
                return url;
            }
        }else if(account instanceof DevAccount){
            if(devAccountService.verifyAccount((DevAccount)account, verificationForm.fill()))
            {
                request.getSession().removeAttribute("account");
                return url;
            }
        }

       
        model.addAttribute("error", "Wrong Code");
        return "verifyAccount";
    }
}