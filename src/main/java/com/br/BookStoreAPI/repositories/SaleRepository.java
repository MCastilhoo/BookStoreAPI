package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.entities.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByUser_UserId(Long userId);
}
