package com.br.BookStoreAPI.models.DTOs.SaleHistoryDTOs;

import com.br.BookStoreAPI.models.entities.UserEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

public record SaleDetailsReponseDTO(
        UserEntity user,
        Integer soldAmount,
        Float totalPrice,
        LocalDateTime sellDate
) implements Serializable {
}
