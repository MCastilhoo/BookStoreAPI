package com.br.BookStoreAPI.factories;

import com.br.BookStoreAPI.models.DTOs.historySaleDTOs.HistorySaleDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.saleDTOs.SaleDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.SaleEntity;

import java.util.stream.Collectors;

public class SaleFactory {
    public static SaleDetailsResponseDTO CreateDetails(SaleEntity saleEntity) {
        return new SaleDetailsResponseDTO(
                saleEntity.getSaleId(),
                saleEntity.getUser().getUserId(),
                saleEntity.getTotalPrice(),
                saleEntity.getStatus().name(),
                saleEntity.getSellDate(),
                saleEntity.getDetails().stream()
                        .map(detail -> new HistorySaleDetailsResponseDTO(
                                detail.getHistorySaleId(),
                                detail.getBook().getBookId(),
                                detail.getBook().getTitle(),
                                detail.getQuantityPurchased(),
                                detail.getPricePerBook(),
                                detail.getTotalAmount()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
