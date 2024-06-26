package com.br.BookStoreAPI.models.DTOs.userDTOs;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO (
        @NotBlank String userName,

        @NotBlank String userEmail,

        @NotBlank String userPassword
){}
