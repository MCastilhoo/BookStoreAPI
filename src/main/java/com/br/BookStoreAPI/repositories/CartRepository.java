package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository <CartEntity, Long>{
    Optional<CartEntity>findByUserUserId(Long userId);
}
