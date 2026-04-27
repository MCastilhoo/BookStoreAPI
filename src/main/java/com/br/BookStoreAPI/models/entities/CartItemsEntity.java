package com.br.BookStoreAPI.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "CART_ITEMS")
public class CartItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ITEM_ID")
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "CART")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "BOOK")
    private BookEntity book;

    @Column(name = "QUANTITY")
    private Integer quantity;
}
