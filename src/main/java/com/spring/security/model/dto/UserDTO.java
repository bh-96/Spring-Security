package com.spring.security.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.security.model.domain.User;
import com.spring.security.utils.Constants;
import com.spring.security.utils.MapperUtils;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Data
public class UserDTO {

    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    @JsonIgnore
    private RoleDTO role;

    public static UserDTO of(User user) {
        return MapperUtils.convert(user, UserDTO.class);
    }

    public UsernamePasswordAuthenticationToken toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(username, password, getAuthorities());
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> Constants.ROLE_USER);
    }

}
