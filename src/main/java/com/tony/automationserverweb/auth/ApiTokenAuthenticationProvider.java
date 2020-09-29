package com.tony.automationserverweb.auth;

import javax.servlet.http.HttpServletRequest;

import com.tony.automationserverweb.model.ApplicationAccountTokenAuthentication;
import com.tony.automationserverweb.model.AuthUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ApiTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthUserService accountUserAuthSerivce;

    @Autowired
    private AuthUserService devAccountUserAuthSerivce;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();

        Authentication auth;
        if (StringUtils.isEmpty(token))
            throw new BadCredentialsException("Authentication failed for " + token);
        else if ('D' == token.charAt(0))
            auth = authenticateDev(authentication);
        else if ('A' == token.charAt(0))
            auth = authenticateAccount(authentication);
        else
            throw new BadCredentialsException("Authentication failed for " + token);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if ("/api/login".equals(request.getRequestURI()) || "/api/dev/login".equals(request.getRequestURI()))
            return false;
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Authentication authenticateDev(Authentication authentication) {
        String token = authentication.getName();

        AuthUser user = devAccountUserAuthSerivce.loadUserByToken(token);
        if (user == null)
            throw new BadCredentialsException("Authentication failed for " + token);

        return createSuccessAuthentication(user, authentication, "D", user.getApplicationToken());
    }

    private Authentication authenticateAccount(Authentication authentication) {
        String token = authentication.getName();
        AuthUser user = accountUserAuthSerivce.loadUserByToken(token);
        if (user == null)
            throw new BadCredentialsException("Authentication failed for " + token);

        return createSuccessAuthentication(user, authentication, "A", user.getApplicationToken());
    }

    private Authentication createSuccessAuthentication(AuthUser user, Authentication authentication, String type,
            String appToken) {
        ApplicationAccountTokenAuthentication result = new ApplicationAccountTokenAuthentication(user.getId(),
                authentication.getCredentials(), user.getAuthorities(), appToken);
        result.setDetails(type);
        return result;
    }

}
