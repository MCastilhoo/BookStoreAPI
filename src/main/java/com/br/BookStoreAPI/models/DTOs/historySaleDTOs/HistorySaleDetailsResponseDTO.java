package com.br.BookStoreAPI.models.DTOs.historySaleDTOs;

import java.io.Serializable;

public record HistorySaleDetailsResponseDTO(
        Long historyId,
        Long bookId,
        String bookTitle,
        Integer quantityPurchased,
        Double pricePerBook,
        Double totalAmount
) implements Serializable {
}
