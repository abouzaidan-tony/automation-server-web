package com.tony.automationserverweb.auth;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.DevAccount;
import com.tony.automationserverweb.service.AccountService;
import com.tony.automationserverweb.service.DevAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class RedirectAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private AccountService accountService;

    @Autowired
    private DevAccountService devAccountService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        String targetUrl = getTargetUrl(request, authentication);

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String getTargetUrl(HttpServletRequest request, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_USER"))
                return "/account";
            else if (grantedAuthority.getAuthority().equals("ROLE_USER_N")){
                Long userId = (Long) authentication.getPrincipal();
                Account account = accountService.getAccountRepositoryImpl().findOneById(userId);
                accountService.sendAccountVerification(account, true);
                HttpSession session = request.getSession(false);
                if(session != null)
                    session.setAttribute("account", account);
                return "/signup/verify";
            }
            
            else if(grantedAuthority.getAuthority().equals("ROLE_DEV"))
                return "/dev/account";
            else if (grantedAuthority.getAuthority().equals("ROLE_DEV_N")){
                Long userId = (Long) authentication.getPrincipal();
                DevAccount account = devAccountService.getDevAccountRepositoryImpl().findOneById(userId);
                devAccountService.sendAccountVerification(account, true);
                HttpSession session = request.getSession(false);
                if(session != null)
                    session.setAttribute("account", account);
                return "/signup/verify";
            }
            
        }

        return "/login";
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
    
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}