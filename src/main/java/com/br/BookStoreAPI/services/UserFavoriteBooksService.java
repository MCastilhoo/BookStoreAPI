package com.br.BookStoreAPI.services;


import com.br.BookStoreAPI.factories.UserFavoriteBooksFactory;
import com.br.BookStoreAPI.models.DTOs.userFavoriteBooksDTOs.UserFavoriteBooksDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.models.entities.UserFavoriteBooksEntity;
import com.br.BookStoreAPI.repositories.BookRepository;
import com.br.BookStoreAPI.repositories.UserFavoriteBooksRepository;
import com.br.BookStoreAPI.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserFavoriteBooksService {
    private final BookRepository bookRepository;
    private final UserFavoriteBooksRepository userFavoriteBooksRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public UserFavoriteBooksService(BookRepository bookRepository, UserFavoriteBooksRepository userFavoriteBooksRepository, UserRepository userRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userFavoriteBooksRepository = userFavoriteBooksRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public UserFavoriteBooksDetailsResponseDTO addFavorite( String slug){
        UserEntity user = userService.getCurrentUser();
        if (userFavoriteBooksRepository.existsByUserUserIdAndBookSlug(user.getUserId(), slug)){
            throw new RuntimeException("User already favorited this book");
        }
        BookEntity book = bookRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Could not find book by slug " + slug));
        UserFavoriteBooksEntity userFavoriteBooksEntity = new UserFavoriteBooksEntity();
        userFavoriteBooksEntity.setBook(book);
        userFavoriteBooksEntity.setUser(user);
        userFavoriteBooksEntity.setFavoriteDate(LocalDateTime.now());
        UserFavoriteBooksEntity saved = userFavoriteBooksRepository.save(userFavoriteBooksEntity);

        return UserFavoriteBooksFactory.createDetails(saved);
    }

    @Transactional
    public void unfavorite(String slug){
        UserEntity user = userService.getCurrentUser();
        if(!userFavoriteBooksRepository.existsByUserUserIdAndBookSlug(user.getUserId(), slug)){
            throw new RuntimeException("User already unfavorited this book");
        }
        userFavoriteBooksRepository.deleteByUserUserIdAndBookSlug(user.getUserId(), slug);

    }

}
