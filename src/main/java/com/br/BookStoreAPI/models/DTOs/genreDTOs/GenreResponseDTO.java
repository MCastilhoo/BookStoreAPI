package com.br.BookStoreAPI.models.DTOs.genreDTOs;

import com.br.BookStoreAPI.models.entities.GenreEntity;

public record GenreResponseDTO(Integer genreId, String genre) {
    public GenreResponseDTO(GenreEntity genre){
        this(genre.getGenreId(), genre.getGenre());
    }
}
