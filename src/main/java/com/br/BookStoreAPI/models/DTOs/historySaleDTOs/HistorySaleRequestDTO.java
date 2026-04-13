package com.br.BookStoreAPI.models.DTOs.historySaleDTOs;

public record HistorySaleRequestDTO(
        Long bookId,
        Integer quantityPurchased
) {
}
