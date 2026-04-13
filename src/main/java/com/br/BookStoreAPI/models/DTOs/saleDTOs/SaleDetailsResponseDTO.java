package com.br.BookStoreAPI.models.DTOs.saleDTOs;

import com.br.BookStoreAPI.models.DTOs.historySaleDTOs.HistorySaleDetailsResponseDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SaleDetailsResponseDTO(
        UUID saleID,
        Long userId,
        Double totalPrice,
        String status,
        LocalDateTime sellDate,
        List<HistorySaleDetailsResponseDTO> items
) implements Serializable {
}
