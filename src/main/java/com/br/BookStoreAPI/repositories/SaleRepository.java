package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.entities.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface
SaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByUser_UserId(Long userId);

    Optional<SaleEntity> findBySaleId(UUID saleId);
}
