package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.entities.HistorySaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleHistoryRepository extends JpaRepository<HistorySaleEntity, Long> {
    List<HistorySaleEntity> findByUser_UserId(Long userId);
}
