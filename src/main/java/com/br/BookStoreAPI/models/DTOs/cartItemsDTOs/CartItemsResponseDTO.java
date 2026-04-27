package com.br.BookStoreAPI.models.DTOs.cartItemsDTOs;

import com.br.BookStoreAPI.models.entities.CartItemsEntity;

import java.io.Serializable;

public record CartItemsResponseDTO(CartItemsEntity cartItems) implements Serializable {
}
