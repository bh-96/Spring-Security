package com.spring.security;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class SecurityApplicationTests {

    @Test
    @Ignore
    public void contextLoads() {
        String password = "testuserbh";
        System.out.println(new BCryptPasswordEncoder().encode(password));
    }

}
