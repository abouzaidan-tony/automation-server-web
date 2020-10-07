package com.tony.automationserverweb.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tony.automationserverweb.model.AccountSession;
import com.tony.automationserverweb.model.ApplicationAccountTokenAuthentication;
import com.tony.automationserverweb.service.AccountSessionService;

import org.apache.logging.log4j.core.util.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

@Component
public class ApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String jsonContentType = "application/json";

    @Autowired
    private AccountSessionService accountSessionService;

    public ApiAuthenticationFilter(AuthenticationManager apiAuthManager) {
        super("/**");
        setAuthenticationManager(apiAuthManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String token = request.getHeader("Token");
        String contentType = request.getHeader("Content-Type");

        logger.info("Intercepting the request");
        boolean isJson = jsonContentType.equals(contentType);

        UsernamePasswordAuthenticationToken authenticationObj = null;
        if (isJson && token == null) {
            logger.info("Authenticating with username and password");
            authenticationObj = getAuthentication(request);
        } else if (isJson) {
            logger.info("Authenticating with header token");
            authenticationObj = getAuthentication(token);
        }

        return getAuthenticationManager().authenticate(authenticationObj);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        logger.info("Authentication successful");
        String token = request.getHeader("Token");
        if (token == null) {
            String applicationToken = null;
            if (authResult instanceof ApplicationAccountTokenAuthentication)
                applicationToken = ((ApplicationAccountTokenAuthentication) authResult).getApplicationToken();

            AccountSession as = accountSessionService.createAccountSession((Long) authResult.getPrincipal(),
                    (String) authResult.getDetails(), applicationToken);

            response.addHeader("Token", as.getToken());
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        logger.debug("Populated SecurityContextHolder with token: '"
                + SecurityContextHolder.getContext().getAuthentication() + "'");

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{\"success\":false}");
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        try {
            String body = IOUtils.toString(request.getReader());

            JSONObject json = new JSONObject(body);

            String email = json.optString("email");
            String password = json.optString("password");
            String appToken = json.optString("application");

            return new ApplicationAccountTokenAuthentication(email, password, appToken);
        } catch (IOException e) {
            logger.error("Error", e);
            return null;
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(token, null);
    }

}
