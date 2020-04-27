package com.spring.security.model.dto;

import com.spring.security.model.domain.Role;
import com.spring.security.utils.MapperUtils;
import lombok.Data;

@Data
public class RoleDTO {

    private int id;
    private String rolename;

    public static RoleDTO of(Role role) {
        return MapperUtils.convert(role, RoleDTO.class);
    }

}
