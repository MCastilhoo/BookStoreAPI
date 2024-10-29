package com.br.BookStoreAPI.models.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "SALES_HISTORY")
public class SaleHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SALE_ID")
    private Long saleId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @OneToMany(mappedBy = "saleHistory", cascade = CascadeType.ALL)
    private List<SaleItemEntity> saleItems;

    @Column(name = "TOTAL_PRICE")
    private double totalPrice;

}
