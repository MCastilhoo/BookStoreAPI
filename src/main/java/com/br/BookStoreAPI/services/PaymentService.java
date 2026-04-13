package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.DTOs.paymentDTO.PaymentRequestDTO;
import com.br.BookStoreAPI.models.entities.SaleEntity;
import com.br.BookStoreAPI.repositories.BookRepository;
import com.br.BookStoreAPI.repositories.SaleRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final SaleRepository saleRepository;
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    private final BookRepository bookRepository;

    public PaymentService(BookRepository bookRepository, SaleRepository saleRepository) {
        this.bookRepository = bookRepository;
        this.saleRepository = saleRepository;
    }
    public String pay(PaymentRequestDTO paymentRequestDTO) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        SaleEntity sale = saleRepository.findById(paymentRequestDTO.saleId())
                .orElseThrow(() -> new IllegalArgumentException("Sale not found"));
        long totalAmount = Math.round(sale.getTotalPrice() * 100);
        PaymentIntentCreateParams paymentParams = PaymentIntentCreateParams.builder()
                .setAmount(totalAmount)
                .setCurrency("brl")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                )
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(paymentParams);
        return paymentIntent.getClientSecret();
    }
}
