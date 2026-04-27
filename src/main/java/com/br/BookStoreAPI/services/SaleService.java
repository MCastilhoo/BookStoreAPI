package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.factories.SaleFactory;
import com.br.BookStoreAPI.models.DTOs.historySaleDTOs.HistorySaleRequestDTO;
import com.br.BookStoreAPI.models.DTOs.saleDTOs.SaleDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.saleDTOs.SaleRequestDTO;
import com.br.BookStoreAPI.models.entities.*;
import com.br.BookStoreAPI.repositories.BookRepository;
import com.br.BookStoreAPI.repositories.CartRepository;
import com.br.BookStoreAPI.repositories.SaleRepository;
import com.br.BookStoreAPI.repositories.UserRepository;
import com.br.BookStoreAPI.utils.enums.SaleStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final HistorySaleService historySaleService;
    private final UserService userService;
    private final CartRepository cartRepository;
    private final CartService cartService;

    @Transactional
    public SaleDetailsResponseDTO createPendingSale(SaleRequestDTO saleDTO) {
        UserEntity user = userService.getCurrentUser();
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
        SaleEntity savedSale = saleRepository.save(saleEntity);
        return SaleFactory.CreateDetails(savedSale);
    }

    @Transactional
    public SaleDetailsResponseDTO checkoutFromCart() {
        UserEntity user = userService.getCurrentUser();
        CartEntity cart = cartRepository.findByUserUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Cart not found for ID: " + user.getUserId()));
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        List<HistorySaleRequestDTO> itemsRequest = cart.getCartItems().stream()
                .map(item -> new HistorySaleRequestDTO(
                        item.getBook().getBookId(),
                        item.getQuantity()
                ))
                .toList();
        SaleRequestDTO saleRequest = new SaleRequestDTO(itemsRequest);
        SaleDetailsResponseDTO response = createPendingSale(saleRequest);
        cartService.clearCart(user.getUserId());
        return response;
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
