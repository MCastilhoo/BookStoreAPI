package com.br.BookStoreAPI.models.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "SALES_HISTORY")
public class SaleHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SALE_ID")
    private UUID saleId;


    @Column(name = "UNIT_QUANTITY")
    private int unitQuantity;

    @Column(name = "UNIT_PRICE")
    private double unitPrice;

    @Column(name = "SOLD_AMOUNT")
    private Float soldAmount;

    @Column(name = "AMOUNT")
    private Float amount;
}
