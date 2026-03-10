package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);


    Optional<BookEntity> findBySlug(String slug);
}
