package com.spring.security.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.model.dto.UserDTO;
import com.spring.security.security.authentication.TokenAuthentication;
import com.spring.security.security.authentication.UserAuthentication;
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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private TokenAuthentication tokenAuthentication;
    private UserService userService;

    public StatelessLoginFilter(String urlMapping, TokenAuthentication tokenAuthentication,
                                UserService userService, AuthenticationManager authenticationManager) {
        super(urlMapping);
        this.tokenAuthentication = tokenAuthentication;
        this.userService = userService;
        setAuthenticationManager(authenticationManager);
    }

    // 로그인
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        final UserDTO user = toUser(request);
        final UsernamePasswordAuthenticationToken loginToken = user.toAuthenticationToken();
        logger.info("Login : " + user.getUsername());
        return getAuthenticationManager().authenticate(loginToken);
    }

    private UserDTO toUser(HttpServletRequest request) throws IOException {
        return new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        try {
            final UserDetails authenticatedUser = userService.loadUserByUsername(authResult.getName());
            final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
            tokenAuthentication.addJwtTokenToHeader(response, userAuthentication);
            SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        } catch (Exception e) {
            logger.error("successfulAuthentication : " + e.getMessage());
        }
    }
}
