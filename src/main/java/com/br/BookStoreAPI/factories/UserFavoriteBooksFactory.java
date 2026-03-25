package com.br.BookStoreAPI.factories;

import com.br.BookStoreAPI.models.DTOs.userFavoriteBooksDTOs.UserFavoriteBooksDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.UserFavoriteBooksEntity;

public class UserFavoriteBooksFactory {
    public static UserFavoriteBooksDetailsResponseDTO createDetails(UserFavoriteBooksEntity entity) {
        return new UserFavoriteBooksDetailsResponseDTO(
                entity.getUserFavoriteBookId(),
                BookFactory.CreateDetails(entity.getBook()),
                entity.getFavoriteDate()
        );
    }
}
