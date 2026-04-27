package com.br.BookStoreAPI.controllers;

import com.br.BookStoreAPI.models.DTOs.cartDTOs.CartDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.cartDTOs.CartResponseDTO;
import com.br.BookStoreAPI.models.DTOs.cartItemsDTOs.CartItemsRequestDTO;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartDetailsResponseDTO> addItem( @Valid @RequestBody CartItemsRequestDTO request){
        CartDetailsResponseDTO response = cartService.addItemToCart( request);
        return ResponseEntity.ok(response);
    }
}
