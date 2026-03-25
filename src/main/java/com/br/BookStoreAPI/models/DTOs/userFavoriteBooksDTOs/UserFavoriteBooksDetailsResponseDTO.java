package com.br.BookStoreAPI.models.DTOs.userFavoriteBooksDTOs;

import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookDetailsResponseDTO;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UserFavoriteBooksDetailsResponseDTO(
        Long userFavoriteBookId,
        BookDetailsResponseDTO book,
        LocalDateTime favoriteDate
) implements Serializable {
}
