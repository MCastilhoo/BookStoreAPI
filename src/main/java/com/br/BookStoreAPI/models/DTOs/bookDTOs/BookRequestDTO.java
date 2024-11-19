package com.br.BookStoreAPI.models.DTOs.bookDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookRequestDTO (

        @NotBlank String title,

        @NotNull int pageNumbers,

        @NotBlank String author,

        @NotBlank String category,

        @NotNull Integer quantity,

        @NotNull Double price


){}
