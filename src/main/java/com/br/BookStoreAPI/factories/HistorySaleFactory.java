package com.br.BookStoreAPI.factories;

import com.br.BookStoreAPI.models.DTOs.historySaleDTOs.HistorySaleDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.historySaleDTOs.HistorySaleResponseDTO;
import com.br.BookStoreAPI.models.entities.HistorySaleEntity;

public class HistorySaleFactory {
    public static HistorySaleDetailsResponseDTO CreateDetails (HistorySaleEntity entity) {
        return new HistorySaleDetailsResponseDTO(
                entity.getHistorySaleId(),
                entity.getBook().getBookId(),
                entity.getBook().getTitle(),
                entity.getQuantityPurchased(),
                entity.getPricePerBook(),
                entity.getTotalAmount()
        );
    }
}
