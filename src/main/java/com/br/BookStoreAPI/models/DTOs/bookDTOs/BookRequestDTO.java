package com.br.BookStoreAPI.models.DTOs.bookDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record BookRequestDTO (

        @NotBlank String title,

        @NotNull int pageNumbers,

        @NotBlank String author,

        @NotNull Integer quantity,

        @NotNull Double price,

        Set<Long> genreIds


){}
