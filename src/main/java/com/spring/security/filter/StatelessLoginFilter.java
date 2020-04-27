package com.spring.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.model.domain.User;
import com.spring.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private UserService userService;

    public StatelessLoginFilter(String urlMapping, UserService userService, AuthenticationManager authenticationManager) {
        super(urlMapping);
        this.userService = userService;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        final User user = toUser(request);
        final UsernamePasswordAuthenticationToken loginToken = user.toAuthenticationToken();

        logger.info("login : " + user.getUsername() + "  ||  " + user.getEncodedPassword());

        final UserDetails authenticatedUser = userService.loadUserByUsername(user.getUsername());
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
//        loginToken.eraseCredentials();

        return getAuthenticationManager().authenticate(loginToken);
    }

    private User toUser(HttpServletRequest request) throws IOException {
        return new ObjectMapper().readValue(request.getInputStream(), User.class);
    }
}
