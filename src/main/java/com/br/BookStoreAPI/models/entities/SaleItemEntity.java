package com.br.BookStoreAPI.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "SALE_ITEM")
public class SaleItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "SALE_ID")
    private SaleHistoryEntity saleHistory;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private BookEntity book;

    @Column(name = "UNI_PRICE")
    private double unitPrice;

    @Column(name = "BOOK_QUANTITY")
    private Integer quantity;

    @Column(name = "TOTAL_PRICE")
    private double totalPrice;


}
