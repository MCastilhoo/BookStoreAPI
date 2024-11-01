package com.br.BookStoreAPI.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "DETAILS_SALE")
public class DetailsSaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DETAIL_ID")
    private Long detailId;

    @ManyToOne
    @JoinColumn(name = "SALE_ID")
    private HistorySaleEntity saleHistory;

    @Column(name = "BOOK_ID")
    private String bookId;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "PRICE_PER_BOOK")
    private double pricePerBook;

    @Column(name = "TOTAL_BOOK_PRICE")
    private double totalBookPrice;
}
