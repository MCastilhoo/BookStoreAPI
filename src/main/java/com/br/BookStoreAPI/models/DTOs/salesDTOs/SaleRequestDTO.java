package com.br.BookStoreAPI.models.DTOs.salesDTOs;

import com.br.BookStoreAPI.models.DTOs.detailsSaleDTOs.DetailsSaleRequestDTO;

import java.util.List;

public record SaleRequestDTO(List<DetailsSaleRequestDTO> books) {
}
