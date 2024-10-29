package com.br.BookStoreAPI.models.DTOs.errorsDTOs;

public class ErrorResponseDTO {
    private String message;

    public ErrorResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
