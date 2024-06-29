package com.br.BookStoreAPI.models.DTOs.userDTOs;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO (
        @NotBlank String userFirstName,

        @NotBlank String userLastName,

        @NotBlank String userEmail,

        @NotBlank String userPassword
){}
