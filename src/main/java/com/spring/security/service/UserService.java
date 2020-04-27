package com.spring.security.service;

import com.spring.security.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDTO findByUsername(String username) throws Exception;

    UserDTO register(UserDTO userDTO) throws Exception;

}
