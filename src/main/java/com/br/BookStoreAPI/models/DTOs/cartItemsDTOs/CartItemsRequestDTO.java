package com.br.BookStoreAPI.models.DTOs.cartItemsDTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemsRequestDTO(
        @NotNull Long bookId,
        @NotNull @Positive Integer quantity
) {
}
