package com.br.BookStoreAPI.models.DTOs.userDTOs;

import com.br.BookStoreAPI.models.entities.UserEntity;

import java.io.Serializable;

public record UserResponseDTO(UserEntity userEntity) implements Serializable {
}
