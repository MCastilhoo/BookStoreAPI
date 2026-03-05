package com.br.BookStoreAPI.services;


import com.br.BookStoreAPI.factories.BookFactory;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookRequestDTO;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.GenreEntity;
import com.br.BookStoreAPI.repositories.BookRepository;
import com.br.BookStoreAPI.repositories.GenreRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired

    private BookRepository bookRepository;
    private GenreRepository genreRepository;
    private CloudinaryService cloudinaryService;

    public BookService(BookRepository bookRepository, GenreRepository genreRepository, CloudinaryService cloudinaryService) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public BookResponseDTO create(BookRequestDTO bookRequestDTO, MultipartFile image) throws IOException {
        if (bookRequestDTO.price() == null || bookRequestDTO.price() <= 0){
            throw new IllegalArgumentException("O livro não pode ser cadastrado sem preço ou com preço negativo");
        }
        if (bookRequestDTO.quantity() == null || bookRequestDTO.quantity() <= 0){
            throw new IllegalArgumentException("O livro não pode ser cadastrado sem nenhuma quantidade no estoque");
        }
        String imageUrl = cloudinaryService.uploadImage(image);

        BookEntity book = new BookEntity();
        if (bookRequestDTO.genreIds()!=null && !bookRequestDTO.genreIds().isEmpty()){
            Set<GenreEntity> genres = new HashSet<>(genreRepository.findAllById(bookRequestDTO.genreIds()));
            if (genres.size() != bookRequestDTO.genreIds().size()){
                throw new IllegalArgumentException("deu ruim!");
            }
            book.setGenres(genres);
        }
        book.setBookCover(imageUrl);
        BeanUtils.copyProperties(bookRequestDTO, book);
        BookEntity result = bookRepository.save(book);

        return new BookResponseDTO(result);
    }

    public List<BookResponseDTO> getBooksByFilters(String title, String author){
        List<BookEntity> results;

        if(title != null && !title.isEmpty()){
            results = bookRepository.findByTitleContainingIgnoreCase(title);
        } else if (author != null && !author.isEmpty()){
            results = bookRepository.findByAuthorContainingIgnoreCase(author);
        } else {
            results = Collections.emptyList();
        }
        return results.stream()
                .map(BookResponseDTO::new)
                .collect(Collectors.toList());
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

        BookEntity book = result.get();

        book.setTitle(bookRequestDTO.title());
        book.setPageNumbers(bookRequestDTO.pageNumbers());
        book.setAuthor(bookRequestDTO.author());
        book.setQuantity(bookRequestDTO.quantity());
        book.setPrice(bookRequestDTO.price());
        Set<GenreEntity> genres = new HashSet<>(genreRepository.findAllById(bookRequestDTO.genreIds()));
        book.setGenres(genres);

        BookEntity saved = bookRepository.save(book);

        return new BookResponseDTO(saved);
    }


    public boolean delete(Long bookId) {
        Optional<BookEntity> result = bookRepository.findById(bookId);
        if (result.isEmpty()) return false;
        bookRepository.delete(result.get());
        return true;
    }

}