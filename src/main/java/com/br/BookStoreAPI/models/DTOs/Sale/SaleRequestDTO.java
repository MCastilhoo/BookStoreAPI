package com.br.BookStoreAPI.models.DTOs.Sale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleRequestDTO (
        @NotNull Integer bookId,
        @NotNull @Positive Integer soldAmount
){
}
