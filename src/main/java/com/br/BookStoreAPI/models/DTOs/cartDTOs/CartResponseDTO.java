package com.br.BookStoreAPI.models.DTOs.cartDTOs;

import com.br.BookStoreAPI.models.entities.CartEntity;

import java.io.Serializable;

public record CartResponseDTO (CartEntity cart) implements Serializable {}
