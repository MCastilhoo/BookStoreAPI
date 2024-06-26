package com.br.BookStoreAPI.repositories;


import com.br.BookStoreAPI.models.entities.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, UUID> {
}
