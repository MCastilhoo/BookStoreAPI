package com.br.BookStoreAPI.models.DTOs.adminDTOs;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record AdminDetailsResponseDTO(

        UUID userId,
        String administratorName,
        String administratorSurname,
        String administratorEmail,
        String administratorPassword,
        LocalDateTime creationDate,
        LocalDateTime modificationDate
) implements Serializable {
}
