package com.br.BookStoreAPI.controllers;

import com.br.BookStoreAPI.factories.PaymentFactory;
import com.br.BookStoreAPI.models.DTOs.paymentDTO.PaymentRequestDTO;
import com.br.BookStoreAPI.models.DTOs.paymentDTO.PaymentResponseDTO;
import com.br.BookStoreAPI.services.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponseDTO> checkout(@RequestBody @Valid PaymentRequestDTO paymentRequestDTO) throws StripeException {
        String clientSecret = paymentService.pay(paymentRequestDTO);
        PaymentResponseDTO response = PaymentFactory.createResponse(clientSecret);
        return ResponseEntity.ok(response);
    }
}