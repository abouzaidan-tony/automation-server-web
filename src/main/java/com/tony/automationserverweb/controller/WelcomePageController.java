package com.tony.automationserverweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelcomePageController {

    @RequestMapping(value ={"", "/"})
    public String welcomePage(){
        return "redirect:/login";
    }

    @RequestMapping("/dev")
    public String devWelcomePage(){
        return "redirect:/dev/login";
    }


    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        String errorMessge = null;
        if (error != null) {
            errorMessge = "Username or Password is incorrect !!";
        }

        model.addAttribute("signInTxt", "Sign in as developer");
        model.addAttribute("signInUrl", "/dev/login");
        model.addAttribute("signUpUrl", "/signup");
        model.addAttribute("formSubmit", "/postLogin");



        model.addAttribute("title", "Membre Login");
        
        model.addAttribute("errorMessge", errorMessge);
        return "login";
    }

    @RequestMapping("/dev/login")
    public String devLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        String errorMessge = null;
        if (error != null) {
            errorMessge = "Username or Password is incorrect !!";
        }

        model.addAttribute("signInTxt", "Sign in as user");
        model.addAttribute("signInUrl", "/login");
        model.addAttribute("signUpUrl", "/dev/signup");
        model.addAttribute("formSubmit", "/dev/postLogin");
        
        model.addAttribute("title", "Developer Login");

        model.addAttribute("errorMessge", errorMessge);
        return "login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }
}