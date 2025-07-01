package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.entities.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    Optional<GenreEntity> findByGenre(String genre);
}
