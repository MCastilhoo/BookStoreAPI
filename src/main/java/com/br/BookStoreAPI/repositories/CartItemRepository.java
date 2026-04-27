package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.entities.CartItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository <CartItemsEntity, Long>{
    void deleteByCartCartId(Long cartId);
}
