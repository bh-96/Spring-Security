package com.spring.security.security.authentication;

import com.spring.security.service.UserService;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenHandler {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenHandler.class);

    private final String secret;
    private final UserService userService;

    @Autowired
    public JwtTokenHandler(@Value("${jwt.secret}") String secret, UserService userService) {
        this.secret = secret;
        this.userService = userService;
    }

    // 토큰 생성
    public String createTokenForUser(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS512, secret)
                //NOTE:security active time 설정(토큰 유효 시간 설정)
                .setExpiration(DateUtils.addHours(new Date(), 6))
                .compact();
    }

    public Optional<UserDetails> parseUserFromToken(String token) throws Exception{
        try {
            String username = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            return Optional.ofNullable(userService.loadUserByUsername(username));
        } catch (ExpiredJwtException | UnsupportedJwtException | SignatureException |IllegalArgumentException | UsernameNotFoundException e) {
            return Optional.empty();
        }
    }
}