package com.br.BookStoreAPI.models.DTOs.userFavoriteBooksDTOs;

import com.br.BookStoreAPI.models.entities.UserFavoriteBooksEntity;

import java.io.Serializable;

public record UserFavoriteBooksResponseDTO(UserFavoriteBooksEntity userFavoriteBooksEntity) implements Serializable {
}
