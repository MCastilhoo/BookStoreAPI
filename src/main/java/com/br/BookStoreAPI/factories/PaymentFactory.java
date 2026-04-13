package com.br.BookStoreAPI.factories;

import com.br.BookStoreAPI.models.DTOs.paymentDTO.PaymentResponseDTO;

public class PaymentFactory {
    public static PaymentResponseDTO createResponse(String clientSecret){
        return new PaymentResponseDTO(
                clientSecret,
                "teste de mensagem, troque para uma mensagem do vendedor ao terminar a implementação, no PaymentFactory!"
        );
    }
}
