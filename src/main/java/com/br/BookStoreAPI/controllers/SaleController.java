package com.br.BookStoreAPI.controllers;

import com.br.BookStoreAPI.models.DTOs.saleDTOs.SaleRequestDTO;
import com.br.BookStoreAPI.models.DTOs.saleDTOs.SaleDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.SaleEntity;
import com.br.BookStoreAPI.services.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/buy-now")
    public ResponseEntity<SaleDetailsResponseDTO> createSale(@Valid @RequestBody SaleRequestDTO saleRequestDTO) {
        SaleDetailsResponseDTO response = saleService.createPendingSale(saleRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/checkout-cart")
    public ResponseEntity<SaleDetailsResponseDTO> checkoutCart(){
        SaleDetailsResponseDTO response = saleService.checkoutFromCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
