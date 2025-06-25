package com.br.BookStoreAPI.models.DTOs.userDTOs;
import java.util.Set;

import jakarta.validation.constraints.*;

public record UserRequestDTO (
        @NotBlank String firstName,

        @NotBlank String lastName,


        @Email  @NotBlank String userEmail,

        @NotBlank String password,

        @NotBlank Set<Long> roleIds
){}
