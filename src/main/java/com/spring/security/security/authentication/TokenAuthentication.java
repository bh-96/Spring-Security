package com.spring.security.security.authentication;

import com.spring.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.spring.security.utils.Constants.*;

@Service
public class TokenAuthentication {

    private UserService userService;
    private JwtTokenHandler jwtTokenHandler;

    @Autowired
    public TokenAuthentication(UserService userService, JwtTokenHandler jwtTokenHandler) {
        this.userService = userService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    public void addJwtTokenToHeader(HttpServletResponse response,
                                    UserAuthentication authentication) throws Exception {

        final UserDetails userDetails = authentication.getDetails();

        if (userDetails == null) {
            throw new UsernameNotFoundException(userDetails.getUsername());
        }

        UserDetails user = userService.loadUserByUsername(userDetails.getUsername());
        response.setHeader(AUTH_HEADER_NAME, jwtTokenHandler.createTokenForUser(userDetails));

        String roleName = userService.findByUsername(user.getUsername()).getRole().getRolename();

        if (roleName.equals(ROLE_ADMIN)) {
            response.setHeader(AUTH_HEADER_ROLE, ROLE_ADMIN);
        } else if (roleName.equals(ROLE_USER)) {
            response.setHeader(AUTH_HEADER_ROLE, ROLE_USER);
        } else {
            response.setHeader(AUTH_HEADER_ROLE, ROLE_DEV);
        }
    }

    public Authentication generateAuthenticationFromRequest(HttpServletRequest request) throws Exception {
        try {
            final String token = request.getHeader(AUTH_HEADER_NAME);

            if (token == null || token.isEmpty()) {
                return null;
            }

            Optional<UserDetails> userDetails = jwtTokenHandler.parseUserFromToken(token);

            if (userDetails.isPresent()) {
                return new UserAuthentication(userDetails.get());
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
