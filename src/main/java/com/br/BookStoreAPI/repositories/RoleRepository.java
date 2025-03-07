package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.entities.RoleEntity;
import com.br.BookStoreAPI.utils.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRole(RoleType role);

    default RoleEntity findByRoleName(String roleName) {
        return findByRole(RoleType.valueOf(roleName.toUpperCase()));
    }
}