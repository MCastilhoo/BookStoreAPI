package com.br.BookStoreAPI.models.DTOs.historySaleDTOs;

import java.io.Serializable;
import java.util.UUID;

public record HistorySaleDetailsResponseDTO(
        UUID historyId,
        Long bookId,
        String bookTitle,
        Integer quantityPurchased,
        Double pricePerBook,
        Double totalAmount
) implements Serializable {
}
