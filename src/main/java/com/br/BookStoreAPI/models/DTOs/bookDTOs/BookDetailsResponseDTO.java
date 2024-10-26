package com.br.BookStoreAPI.models.DTOs.bookDTOs;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookDetailsResponseDTO(

        UUID bookId,
        String title,
        int pageNumbers,
        String author,
        float price,
        LocalDateTime creationDate,
        LocalDateTime modificationDate

) implements Serializable {
}
