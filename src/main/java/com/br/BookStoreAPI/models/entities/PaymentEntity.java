package com.br.BookStoreAPI.models.entities;

import com.br.BookStoreAPI.utils.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PAYMENT_ID")
    private UUID paymentId;

    @ManyToOne
    @JoinColumn(name = "SALE_ID")
    private SaleEntity sale;

    @Column(name = "PAYMENT_INTENT_ID")
    private String stripePaymentIntentId;

    @Column(name = "AMOUNT")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS")
    private PaymentStatus paymentStatus;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "PAYMENT_DATE")
    private LocalDate paymentDate;
}
