package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.entities.SaleHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleHistoryRepository extends JpaRepository<SaleHistoryEntity, Long> {
    List<SaleHistoryEntity> findByUserId(Long userId);
}
