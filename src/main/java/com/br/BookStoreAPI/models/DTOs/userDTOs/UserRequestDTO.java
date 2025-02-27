package com.br.BookStoreAPI.models.DTOs.userDTOs;
import java.util.Set;

import com.br.BookStoreAPI.models.entities.RoleEntity;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO (
        @NotBlank String firstName,

        @NotBlank String lastName,

        @NotBlank String userEmail,

        @NotBlank String password,

        @NotBlank Set<Long> roleIds
){}
