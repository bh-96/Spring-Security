package com.spring.security.model.domain;

import com.spring.security.model.dto.RoleDTO;
import com.spring.security.utils.MapperUtils;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "ROLE")
@ToString(exclude = "userRole")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String rolename;

    @OneToMany
    @JoinTable(name = "USER_ROLE",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> userRole;

    public static Role of(RoleDTO roleDTO) {
        return MapperUtils.convert(roleDTO, Role.class);
    }

}