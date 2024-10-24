package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRole(RoleEntity.RoleType role);

    default RoleEntity findByRoleName(String roleName) {
        return findByRole(RoleEntity.RoleType.valueOf(roleName.toUpperCase()));
    }
}