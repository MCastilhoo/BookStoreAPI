package com.br.BookStoreAPI.models.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
public class RoleEntity {
    @Id
    @Column(name = "ROLE_ID")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private RoleType role;

    public enum RoleType {
        ADMIN(1L),
        EMPLOYEE(2L),
        USER(3L);

        private final long roleId;

        RoleType(long roleId) {this.roleId = roleId;}

        public long getRoleId() {return roleId;}
    }
}
