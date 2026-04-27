package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.factories.CartFactory;
import com.br.BookStoreAPI.models.DTOs.cartDTOs.CartDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.cartItemsDTOs.CartItemsRequestDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.CartEntity;
import com.br.BookStoreAPI.models.entities.CartItemsEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.BookRepository;
import com.br.BookStoreAPI.repositories.CartRepository;
import com.br.BookStoreAPI.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserService userService;

    public CartDetailsResponseDTO addItemToCart( CartItemsRequestDTO itemsRequestDTO){
        UserEntity user = userService.getCurrentUser();
        CartEntity cart = cartRepository.findByUserUserId(user.getUserId())
                .orElseGet(() -> createNewCart(user.getUserId()));
        BookEntity book = bookRepository.findById(itemsRequestDTO.bookId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado na nossa base de dados."));
        Optional<CartItemsEntity> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getBookId().equals(itemsRequestDTO.bookId()))
                .findFirst();
        if (existingItem.isPresent()){
            CartItemsEntity items = existingItem.get();
            items.setQuantity(items.getQuantity() + itemsRequestDTO.quantity());
        } else {
            CartItemsEntity newItem = new CartItemsEntity();
            newItem.setCart(cart);
            newItem.setBook(book);
            newItem.setQuantity(itemsRequestDTO.quantity());
            cart.getCartItems().add(newItem);
        }
        CartEntity savedCart = cartRepository.save(cart);
        return CartFactory.createDetails(savedCart);
    }

    public CartDetailsResponseDTO getMyAllItems(){
        UserEntity user = userService.getCurrentUser();
        return cartRepository.findByUserUserId(user.getUserId())
                .map(CartFactory::createDetails)
                .orElseGet(() -> {
                    return new CartDetailsResponseDTO(null, user.getUserId(), Collections.emptyList(), 0.0);
                });
    }
    private CartEntity createNewCart(Long userId){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + userId + " não existe"));
        CartEntity newCart = new CartEntity();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }
}
