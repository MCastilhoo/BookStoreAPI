package com.br.BookStoreAPI.models.DTOs.userDTOs;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserDetailsResponseDTO(

        Long userId,
        String userFirstName,
        String userLastName,
        String userEmail,
        String userPassword,
        LocalDateTime creationDate,
        LocalDateTime modificationDate
) implements Serializable {
}
