package com.br.BookStoreAPI.models.DTOs.cartItemsDTOs;

import java.io.Serializable;

public record CartItemsDetailsResponseDTO(
        Long cartItemId,
        Long bookId,
        String bookTitle,
        String bookCover,
        Double unitPrice,
        Integer quantity,
        Double subTotal
) implements Serializable {
}
