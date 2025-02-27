package com.br.BookStoreAPI.controllers;

import com.br.BookStoreAPI.models.DTOs.salesDTOs.SaleRequestDTO;
import com.br.BookStoreAPI.models.DTOs.salesDTOs.SaleResponseDTO;
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

    @PostMapping
    public ResponseEntity<Object> createSale(@Valid @RequestBody SaleRequestDTO saleRequestDTO) {
        try {
            SaleResponseDTO responseDTO = saleService.createSale(saleRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
