package com.br.BookStoreAPI.models.DTOs.bookDTOs;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record BookDetailsResponseDTO(

        Long bookId,
        String bookCover,
        String title,
        int pageNumbers,
        String author,
        List<String> genres,
        Double price,
        String slug,
        LocalDateTime creationDate,
        LocalDateTime modificationDate


) implements Serializable {
}
