package com.br.BookStoreAPI.models.DTOs.paymentDTO;

import java.io.Serializable;

public record PaymentResponseDTO(
        String clientSecret,
        String customerMessage
) implements Serializable {
}
