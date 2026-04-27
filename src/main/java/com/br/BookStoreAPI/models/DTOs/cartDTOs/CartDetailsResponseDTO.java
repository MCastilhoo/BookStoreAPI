package com.br.BookStoreAPI.models.DTOs.cartDTOs;

import com.br.BookStoreAPI.models.DTOs.cartItemsDTOs.CartItemsDetailsResponseDTO;

import java.io.Serializable;
import java.util.List;

public record CartDetailsResponseDTO(
        Long cartId,
        Long userId,
        List<CartItemsDetailsResponseDTO> items,
        Double totalCartPrice
) implements Serializable {
}
