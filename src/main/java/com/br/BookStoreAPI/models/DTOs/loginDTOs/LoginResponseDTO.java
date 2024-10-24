package com.br.BookStoreAPI.models.DTOs.loginDTOs;

public record LoginResponseDTO(String accessToken, Long expiresIn) {
}
