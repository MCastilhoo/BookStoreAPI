package com.br.BookStoreAPI.models.DTOs.salesDTOs;

import com.br.BookStoreAPI.models.entities.SaleEntity;

import java.io.Serializable;

public record SaleResponseDTO (SaleEntity saleEntity) implements Serializable {
}
