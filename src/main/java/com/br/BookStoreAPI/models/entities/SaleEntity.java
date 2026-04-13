package com.br.BookStoreAPI.models.entities;

import com.br.BookStoreAPI.utils.enums.SaleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "SALE")
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "SALE_ID")
    private UUID saleId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private SaleStatus status;

    @Column(name = "STRIPE_PAYMENT_ID")
    private String stripePaymentId;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<HistorySaleEntity> details;

    @Column(name = "SELL_DATE")
    private LocalDateTime sellDate;
}
