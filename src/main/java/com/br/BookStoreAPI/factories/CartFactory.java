package com.br.BookStoreAPI.factories;

import com.br.BookStoreAPI.models.DTOs.cartDTOs.CartDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.cartItemsDTOs.CartItemsDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.CartEntity;

import java.util.List;

public class CartFactory {
    public static CartDetailsResponseDTO createDetails(CartEntity cart){
        List<CartItemsDetailsResponseDTO> items = cart.getCartItems()
                .stream()
                .map(CartItemsFactory::createDetails)
                .toList();
        Double total = items.stream()
                .mapToDouble(CartItemsDetailsResponseDTO::subTotal)
                .sum();
        return new CartDetailsResponseDTO(
                cart.getCartId(),
                cart.getUser().getUserId(),
                items,
                total
        );
    }
}
