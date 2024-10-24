package com.br.BookStoreAPI.models.DTOs.loginDTOs;

import java.io.Serializable;

public record LoginRequestDTO(String userEmail, String password) implements Serializable {
}
