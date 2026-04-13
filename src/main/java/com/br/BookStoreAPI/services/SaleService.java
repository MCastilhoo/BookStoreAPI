package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.DTOs.saleDTOs.SaleRequestDTO;
import com.br.BookStoreAPI.models.DTOs.saleDTOs.SaleDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.HistorySaleEntity;
import com.br.BookStoreAPI.models.entities.SaleEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.BookRepository;
import com.br.BookStoreAPI.repositories.SaleRepository;
import com.br.BookStoreAPI.repositories.UserRepository;
import com.br.BookStoreAPI.utils.enums.RoleType;
import com.br.BookStoreAPI.utils.enums.SaleStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final HistorySaleService historySaleService;

    public SaleService(SaleRepository saleRepository, BookRepository bookRepository, UserRepository userRepository, HistorySaleService historySaleService) {
        this.saleRepository = saleRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.historySaleService = historySaleService;
    }

    @Transactional
    public SaleEntity createPendingSale(SaleRequestDTO saleDTO) {
        Long userId = extractUserIdFromToken();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        RoleType role = user.getRole().getRole();
        if (!(role.equals(RoleType.ADMIN) || role.equals(RoleType.USER))) {
            throw new AccessDeniedException("User does not have the required role to perform this operation.");
        }
        SaleEntity saleEntity = new SaleEntity();
        saleEntity.setUser(user);
        saleEntity.setStatus(SaleStatus.PENDING);
        saleEntity.setSellDate(LocalDateTime.now());
        saleEntity.setDetails(new ArrayList<>());

        double totalSalePrice = 0.0;

        for (var item : saleDTO.books()) {
            BookEntity book = bookRepository.findById(item.bookId())
                    .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + item.bookId()));
            if (book.getQuantity() < item.quantityPurchased()) {
                throw new IllegalArgumentException("Not enough stock for book with id: " + item.bookId());
            }
            HistorySaleEntity historySaleEntity = historySaleService.createHistory(saleEntity, book, item.quantityPurchased());
            saleEntity.getDetails().add(historySaleEntity);
            totalSalePrice += historySaleEntity.getTotalAmount();
        }
        saleEntity.setTotalPrice(totalSalePrice);
        return saleRepository.save(saleEntity);
    }

    @Transactional
    public void confirmPayment(UUID saleId, String stripePaymentId){
        SaleEntity sale = saleRepository.findBySaleId(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        if (sale.getStatus() == SaleStatus.PAID) return;

        for (var detail : sale.getDetails()) {
            BookEntity book = detail.getBook();
            book.setQuantity(book.getQuantity() - detail.getQuantityPurchased());
            bookRepository.save(book);
        }
        sale.setStatus(SaleStatus.PAID);
        sale.setStripePaymentId(stripePaymentId);
        saleRepository.save(sale);
    }

    private Double calculatePricePerBook(BookEntity book) {
        return book.getPrice();
    }

    public Long extractUserIdFromToken() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = (String) jwt.getClaims().get("sub");
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        return user.getUserId();
    }
}
