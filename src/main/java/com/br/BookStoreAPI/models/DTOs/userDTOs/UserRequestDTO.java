package com.br.BookStoreAPI.models.DTOs.userDTOs;
import java.util.Set;

import com.br.BookStoreAPI.models.entities.RoleEntity;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO (
        @NotBlank String userFirstName,

        @NotBlank String userLastName,

        @NotBlank String userEmail,

        @NotBlank String userPassword,

        @NotBlank Set<RoleEntity> userRoles
){}
