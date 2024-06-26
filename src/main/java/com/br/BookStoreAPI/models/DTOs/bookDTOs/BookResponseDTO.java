package com.br.BookStoreAPI.models.DTOs.bookDTOs;

import com.br.BookStoreAPI.models.entities.BookEntity;

import java.io.Serializable;

public record BookResponseDTO(BookEntity bookEntity) implements Serializable {
}
