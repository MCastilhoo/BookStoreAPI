package com.br.BookStoreAPI.models.DTOs.adminDTOs;

import com.br.BookStoreAPI.models.entities.AdminEntity;

import java.io.Serializable;

public record AdminResponseDTO(AdminEntity adminEntity) implements Serializable {
}
