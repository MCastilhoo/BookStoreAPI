package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.DTOs.genreDTOs.GenreRequestDTO;
import com.br.BookStoreAPI.models.DTOs.genreDTOs.GenreResponseDTO;
import com.br.BookStoreAPI.models.entities.GenreEntity;
import com.br.BookStoreAPI.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    @Autowired
    private GenreRepository genreRepository;

    public GenreResponseDTO createGenre(GenreRequestDTO dto) {
        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setGenre(dto.genre());
        GenreEntity savedGenre = genreRepository.save(genreEntity);
        return new GenreResponseDTO(savedGenre);
    }

    public List<GenreResponseDTO> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(GenreResponseDTO::new)
                .toList();
    }
}
