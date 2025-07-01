package com.br.BookStoreAPI.factories;

import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;

import java.util.stream.Collectors;

public class BookFactory {
    public static BookDetailsResponseDTO CreateDetails(BookEntity bookEntity) {
        return new BookDetailsResponseDTO(
                bookEntity.getBookId(),
                bookEntity.getTitle(),
                bookEntity.getPageNumbers(),
                bookEntity.getAuthor(),
                bookEntity.getGenres()
                        .stream()
                        .map(genre -> genre.getGenre())
                        .collect(Collectors.toList()),

                bookEntity.getPrice(),
                bookEntity.getCreationDate(),
                bookEntity.getModificationDate()
        );
    }
}