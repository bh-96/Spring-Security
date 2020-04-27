package com.spring.security.controller;

import com.spring.security.model.dto.UserDTO;
import com.spring.security.service.UserService;
import com.spring.security.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController extends LoggerUtils {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) throws Exception {
        return new ResponseEntity<>(userService.register(userDTO), HttpStatus.OK);
    }

}
