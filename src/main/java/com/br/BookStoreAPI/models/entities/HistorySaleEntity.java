package com.br.BookStoreAPI.models.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "SALES_HISTORY")
public class HistorySaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SALE_ID")
    private Long saleId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @OneToMany(mappedBy = "saleHistory", cascade = CascadeType.ALL)
    private List<DetailsSaleEntity> saleItems;

    @Column(name = "TOTAL_PRICE")
    private double totalPrice;

    @Column(name = "SELL_DATE")
    LocalDateTime sellDate;
}
