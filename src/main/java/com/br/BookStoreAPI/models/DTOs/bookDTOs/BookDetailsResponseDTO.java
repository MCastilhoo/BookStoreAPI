package com.br.BookStoreAPI.models.DTOs.bookDTOs;

import java.io.Serializable;
import java.time.LocalDateTime;

public record BookDetailsResponseDTO(

        Long bookId,
        String title,
        int pageNumbers,
        String author,
        float price,
        LocalDateTime creationDate,
        LocalDateTime modificationDate

) implements Serializable {
}
