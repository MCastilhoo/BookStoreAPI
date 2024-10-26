package com.br.BookStoreAPI.factories;

import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;

public class BookFactory {
    public static BookDetailsResponseDTO CreateDetails(BookEntity bookEntity) {
        return new BookDetailsResponseDTO(
                bookEntity.getBookId(),
                bookEntity.getTitle(),
                bookEntity.getPageNumbers(),
                bookEntity.getAuthor(),
                bookEntity.getPrice(),
                bookEntity.getCreationDate(),
                bookEntity.getModificationDate()
        );
    }
}
