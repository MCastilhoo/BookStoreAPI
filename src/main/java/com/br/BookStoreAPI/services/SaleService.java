package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.DTOs.salesDTOs.SaleRequestDTO;
import com.br.BookStoreAPI.models.DTOs.salesDTOs.SaleResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.SaleEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.BookRepository;
import com.br.BookStoreAPI.repositories.SaleRepository;
import com.br.BookStoreAPI.repositories.UserRepository;
import com.br.BookStoreAPI.utils.enums.RoleType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public SaleService(SaleRepository saleRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.saleRepository = saleRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO saleDTO, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        RoleType role = user.getRole().getRole();
        if (!(role.equals(RoleType.ADMIN) || role.equals(RoleType.USER))) {
            throw new AccessDeniedException("User does not have the required role to perform this operation.");
        }
        BookEntity book = bookRepository.findById(saleDTO.bookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + saleDTO.bookId()));
        SaleEntity saleEntity = new SaleEntity();
        saleEntity.setUser(user);
        saleEntity.setTotalPrice(book.getPrice() * saleDTO.quantityPurchased());
        saleEntity.setSellDate(LocalDateTime.now());
        SaleEntity savedSale = saleRepository.save(saleEntity);
        return new SaleResponseDTO(savedSale);
    }

    public Long extractUserIdFromToken() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.valueOf((String) jwt.getClaims().get("sub"));
    }
}
