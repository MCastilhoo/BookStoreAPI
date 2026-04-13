package com.br.BookStoreAPI.models.DTOs.historySaleDTOs;

import com.br.BookStoreAPI.models.entities.HistorySaleEntity;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record HistorySaleResponseDTO(HistorySaleEntity historySaleEntity) implements Serializable {
}
