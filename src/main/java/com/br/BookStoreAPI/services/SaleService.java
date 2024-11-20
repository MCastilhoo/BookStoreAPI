package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.DTOs.salesDTOs.SaleRequestDTO;
import com.br.BookStoreAPI.models.DTOs.salesDTOs.SaleResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.DetailsSaleEntity;
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
import java.util.ArrayList;

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
    public SaleResponseDTO createSale(SaleRequestDTO saleDTO) {
        Long userId = extractUserIdFromToken();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        RoleType role = user.getRole().getRole();
        if (!(role.equals(RoleType.ADMIN) || role.equals(RoleType.USER))) {
            throw new AccessDeniedException("User does not have the required role to perform this operation.");
        }

        SaleEntity saleEntity = new SaleEntity();
        saleEntity.setDetails(new ArrayList<>());
        saleEntity.setUser(user);
        saleEntity.setSellDate(LocalDateTime.now());

        double totalSalePrice = 0.0;

        for (var item : saleDTO.books()) {
            BookEntity book = bookRepository.findById(item.bookId())
                    .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + item.bookId()));
            if (book.getQuantity() < item.quantityPurchased()) {
                throw new IllegalArgumentException("Not enough stock for book with id: " + item.bookId());
            }
            int updateQuantity = book.getQuantity() - item.quantityPurchased();
            book.setQuantity(updateQuantity);
            bookRepository.save(book);
            DetailsSaleEntity detailsSale = new DetailsSaleEntity();
            detailsSale.setBook(book);
            detailsSale.setQuantityPurchased(item.quantityPurchased());
            // Calcular o preço por livro utilizando o novo método
            Double pricePerBook = calculatePricePerBook(book);
            detailsSale.setPricePerBook(pricePerBook);

            detailsSale.setTotalAmount(pricePerBook * item.quantityPurchased());
            detailsSale.setSale(saleEntity);

            saleEntity.getDetails().add(detailsSale);
            totalSalePrice += detailsSale.getTotalAmount();
        }

        saleEntity.setTotalPrice(totalSalePrice);
        SaleEntity savedSale = saleRepository.save(saleEntity);
        return new SaleResponseDTO(savedSale);
    }

    // Método para calcular o preço por livro
    private Double calculatePricePerBook(BookEntity book) {
        // Aqui você pode fazer algum processamento ou aplicar lógica de desconto, por exemplo
        return book.getPrice();
    }

    public Long extractUserIdFromToken() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.valueOf((String) jwt.getClaims().get("sub"));
    }
}
