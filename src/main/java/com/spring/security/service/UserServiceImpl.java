package com.spring.security.service;

import com.spring.security.model.domain.User;
import com.spring.security.model.dto.RoleDTO;
import com.spring.security.model.dto.UserDTO;
import com.spring.security.model.repository.UserRepository;
import com.spring.security.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.spring.security.utils.Constants.*;

@Service
public class UserServiceImpl extends LoggerUtils implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> user = userRepository.findByUsername(username);
        final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
        user.ifPresent(detailsChecker::check);
        return user.orElseThrow(() -> new UsernameNotFoundException("user not found."));
    }

    @Override
    public UserDTO findByUsername(String username) throws Exception {
        try {
            return UserDTO.of(userRepository.findByUsername(username).get());
        } catch (Exception e) {
            logger.error("findByUsername : " + e.getMessage());
            throw new Exception(e);
        }
    }

    @Override
    public UserDTO register(UserDTO userDTO) throws Exception {
        try {
            User user = User.of(toUserDTO(userDTO));
            return UserDTO.of(userRepository.saveAndFlush(user));
        } catch (Exception e) {
            logger.error("register : " + e.getMessage());
            throw new Exception(e);
        }
    }

    private UserDTO toUserDTO(UserDTO userDTO) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRolename(ROLE_USER);
        userDTO.setRole(roleDTO);
        userDTO.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        return userDTO;
    }
}
