package com.br.BookStoreAPI.factories;

import com.br.BookStoreAPI.models.DTOs.cartItemsDTOs.CartItemsDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.CartItemsEntity;

public class CartItemsFactory {
    public static CartItemsDetailsResponseDTO createDetails(CartItemsEntity items){
        Double unitPrice = items.getBook().getPrice();
        Double totalPrice = unitPrice * items.getQuantity();

        return new CartItemsDetailsResponseDTO(
                items.getCartItemId(),
                items.getBook().getBookId(),
                items.getBook().getTitle(),
                items.getBook().getBookCover(),
                unitPrice,
                items.getQuantity(),
                totalPrice
        );
    }
}
