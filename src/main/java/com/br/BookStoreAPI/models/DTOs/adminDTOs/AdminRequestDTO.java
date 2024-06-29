package com.br.BookStoreAPI.models.DTOs.adminDTOs;


import jakarta.validation.constraints.NotBlank;

public record AdminRequestDTO (

    @NotBlank String administratorFirstName,

    @NotBlank String administatorLastName,

    @NotBlank String administratorEmail,

    @NotBlank String administratorPassword
) {}
