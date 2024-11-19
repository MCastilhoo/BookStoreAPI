package com.br.BookStoreAPI.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "DETAILS_SALE")
public class DetailsSaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DETAIL_SALE_ID")
    private Long detailSaleId;

    @ManyToOne
    @JoinColumn(name = "SALE_ID")
    private SaleEntity sale;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private BookEntity book;

    @Column(name = "PRICE_PER_BOOK")
    private Double pricePerBook;

    @Column(name = "QUANTITY_PURCHASED")
    private Integer quantityPurchased;

    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount;
}
