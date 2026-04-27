package com.br.BookStoreAPI.controllers;

import com.br.BookStoreAPI.models.DTOs.cartDTOs.CartDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.cartItemsDTOs.CartItemsRequestDTO;
import com.br.BookStoreAPI.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/my-cart")
    public ResponseEntity<CartDetailsResponseDTO>myCart(){
        CartDetailsResponseDTO items = cartService.getMyAllItems();
        return ResponseEntity.ok(items);
    }
}
