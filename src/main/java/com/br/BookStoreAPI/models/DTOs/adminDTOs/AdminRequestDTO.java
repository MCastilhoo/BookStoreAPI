package com.br.BookStoreAPI.models.DTOs.adminDTOs;


import jakarta.validation.constraints.NotBlank;

public record AdminRequestDTO (

    @NotBlank String administratorName,

    @NotBlank String administatorSurname,

    @NotBlank String administratorEmail,

    @NotBlank String administratorPassword
) {}
