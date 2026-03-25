package com.br.BookStoreAPI.services;


import com.br.BookStoreAPI.exceptions.CustomException;
import com.br.BookStoreAPI.factories.BookFactory;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookRequestDTO;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.GenreEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.BookRepository;
import com.br.BookStoreAPI.repositories.GenreRepository;
import com.br.BookStoreAPI.repositories.UserFavoriteBooksRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.awt.print.Book;
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
    @Autowired
    private UserService userService;
    @Autowired
    private UserFavoriteBooksRepository userFavoriteBooksRepository;

    public BookService(BookRepository bookRepository, GenreRepository genreRepository, CloudinaryService cloudinaryService, UserService userService) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.cloudinaryService = cloudinaryService;
        this.userService = userService;
    }

    @Transactional
    public BookResponseDTO create(BookRequestDTO bookRequestDTO, MultipartFile image) throws IOException {
        if (bookRequestDTO.price() == null || bookRequestDTO.price() <= 0){
            throw new IllegalArgumentException("O livro não pode ser cadastrado sem preço ou com preço negativo");
        }
        if (bookRequestDTO.quantity() == null || bookRequestDTO.quantity() <= 0){
            throw new IllegalArgumentException("O livro não pode ser cadastrado sem nenhuma quantidade no estoque");
        }
        if (bookRequestDTO.synopsis() == null) {
            throw new IllegalArgumentException("Não podemos criar um livro sem sinopse");
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

    public List<BookDetailsResponseDTO> getBooksByFilters(String q) {
        List<BookEntity> results = bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(q, q);

        if (results.isEmpty()) {
            throw new CustomException("Nenhum livro encontrado.", HttpStatus.NOT_FOUND);
        }

        return results.stream()
                .map(BookFactory::CreateDetails)
                .collect(Collectors.toList());
    }
    public BookResponseDTO getBookById(Long id) {
        Optional<BookEntity> result = bookRepository.findById(id);
        if (result.isEmpty()) return null;
        return new BookResponseDTO(result.get());
    }

    public BookDetailsResponseDTO getBookDetailsBySlug(String slug){
        Optional<BookEntity> bookEntity = bookRepository.findBySlug(slug);
        UserEntity user = userService.getCurrentUser();

        boolean isFavorited = false;
        if (user != null) {
            isFavorited =  userFavoriteBooksRepository
                    .existsByUserUserIdAndBookSlug(user.getUserId(), slug);
        }
        return BookFactory.CreateDetails(bookEntity.get(), isFavorited);

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