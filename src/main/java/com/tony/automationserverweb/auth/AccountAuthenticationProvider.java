package com.tony.automationserverweb.auth;

import javax.servlet.http.HttpServletRequest;

import com.tony.automationserverweb.dao.ApplicationRepositoryImpl;
import com.tony.automationserverweb.helper.Helper;
import com.tony.automationserverweb.model.ApplicationAccountTokenAuthentication;
import com.tony.automationserverweb.model.AuthUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AccountAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthUserService accountUserAuthSerivce;

    @Autowired
    private ApplicationRepositoryImpl applicationRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        AuthUser userDetails = accountUserAuthSerivce.loadUserByUsername(email);

        if (!Helper.EncodingMatches(password, userDetails.getPassword()))
            throw new BadCredentialsException("Authentication failed for " + email);

        if (authentication instanceof ApplicationAccountTokenAuthentication && applicationRepository.findOneByToken(
                ((ApplicationAccountTokenAuthentication) authentication).getApplicationToken()) == null) {
            throw new BadCredentialsException("Authentication failed for " + email);
        }

        return createSuccessAuthentication(userDetails.getId(), authentication, userDetails);
    }

    private Authentication createSuccessAuthentication(Object principal, Authentication authentication, AuthUser user) {
        UsernamePasswordAuthenticationToken result = null;
        if (authentication instanceof ApplicationAccountTokenAuthentication) {
            result = new ApplicationAccountTokenAuthentication(principal, authentication.getCredentials(),
                    user.getAuthorities(),
                    ((ApplicationAccountTokenAuthentication) authentication).getApplicationToken());
        } else {
            result = new UsernamePasswordAuthenticationToken(principal, authentication.getCredentials(), user.getAuthorities());
        }
        result.setDetails("A");
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (!"/postLogin".equals(request.getRequestURI()) && !"/api/login".equals(request.getRequestURI()))
            return false;
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}