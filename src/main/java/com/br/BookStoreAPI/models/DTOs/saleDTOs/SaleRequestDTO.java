package com.br.BookStoreAPI.models.DTOs.saleDTOs;

import com.br.BookStoreAPI.models.DTOs.historySaleDTOs.HistorySaleRequestDTO;

import java.util.List;

public record SaleRequestDTO(List<HistorySaleRequestDTO> books) {
}
