package com.br.BookStoreAPI.models.DTOs.salesDTOs;

import com.br.BookStoreAPI.models.DTOs.DetailsSaleDTOs.DetailsSaleRequestDTO;

import java.util.List;

public record SaleRequestDTO(List<DetailsSaleRequestDTO> books) {
}
