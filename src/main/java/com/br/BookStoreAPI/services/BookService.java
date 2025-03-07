package com.br.BookStoreAPI.services;


import com.br.BookStoreAPI.factories.BookFactory;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookRequestDTO;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.repositories.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponseDTO create(BookRequestDTO bookRequestDTO) {
        if (bookRequestDTO.price() == null || bookRequestDTO.price() <= 0){
            throw new IllegalArgumentException("O livro não pode ser cadastrado sem preço ou com preço negativo");
        }
        if (bookRequestDTO.quantity() == null || bookRequestDTO.quantity() <= 0){
            throw new IllegalArgumentException("O livro não pode ser cadastrado sem nenhuma quantidade no estoque");
        }
        BookEntity book = new BookEntity();
        BeanUtils.copyProperties(bookRequestDTO, book);
        BookEntity result = bookRepository.save(book);

        return new BookResponseDTO(result);
    }

    public BookResponseDTO getBookById(Long id) {
        Optional<BookEntity> result = bookRepository.findById(id);
        if (result.isEmpty()) return null;
        return new BookResponseDTO(result.get());
    }

    public List<BookDetailsResponseDTO> getAll(Pageable pageable) {
        Page<BookEntity> books = bookRepository.findAll(pageable);
        List<BookDetailsResponseDTO> results = books
                .stream()
                .map(BookFactory::CreateDetails)
                .collect(Collectors.toList());

        return results;
    }

    public BookResponseDTO update(BookRequestDTO bookRequestDTO, Long bookId) {
        Optional<BookEntity> result = bookRepository.findById(bookId);
        if (result.isEmpty()) return null;
        result.get().setTitle(bookRequestDTO.title());
        result.get().setPageNumbers(bookRequestDTO.pageNumbers());
        result.get().setAuthor(bookRequestDTO.author());
        result.get().setCategory(bookRequestDTO.category());
        result.get().setPrice(bookRequestDTO.price());

        BookEntity saved = bookRepository.save(result.get());

        return new BookResponseDTO(saved);
    }

    public boolean delete(Long bookId) {
        Optional<BookEntity> result = bookRepository.findById(bookId);
        if (result.isEmpty()) return false;
        bookRepository.delete(result.get());
        return true;
    }

}