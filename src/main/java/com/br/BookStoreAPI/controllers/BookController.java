package com.br.BookStoreAPI.controllers;


import com.br.BookStoreAPI.exceptions.GlobalExceptionHandler;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookRequestDTO;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookResponseDTO;
import com.br.BookStoreAPI.models.DTOs.userFavoriteBooksDTOs.UserFavoriteBooksDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.BookEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.BookRepository;
import com.br.BookStoreAPI.services.BookService;
import com.br.BookStoreAPI.services.UserFavoriteBooksService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserFavoriteBooksService userFavoriteBooksService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> create(@RequestPart("book") @Valid BookRequestDTO dto, @RequestPart("bookCover")MultipartFile image) {
        try {
            BookResponseDTO bookResponseDTO = bookService.create(dto, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating a book: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BookDetailsResponseDTO>> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDetailsResponseDTO>> searchBooks(@RequestParam(value = "q", required = false) String q) {
        List<BookDetailsResponseDTO> books = bookService.getBooksByFilters(q);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/livro/{slug}")
    public ResponseEntity<BookDetailsResponseDTO>getBookBySlug(@PathVariable("slug") String slug) {
        BookDetailsResponseDTO book = bookService.getBookDetailsBySlug(slug);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable Long id) {
        BookResponseDTO responseDTO = bookService.getBookById(id);
        if(responseDTO == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable(value = "id") Long id, @RequestBody @Valid BookRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.update(dto, id));
    }

    @DeleteMapping("/id")
    public ResponseEntity<BookResponseDTO> delete(@PathVariable (value = "id") Long id) {
        if(bookService.delete(id)){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/livro/{slug}/favorite")
    public ResponseEntity <Object> favorite(@PathVariable(value = "slug") String slug) {
        UserFavoriteBooksDetailsResponseDTO userFavoriteBooksResponseDTO = userFavoriteBooksService.addFavorite(slug);
        return ResponseEntity.status(HttpStatus.CREATED).body(userFavoriteBooksResponseDTO);
    }

    @DeleteMapping("/livro/{slug}/unfavorite")
    public ResponseEntity <Object> unfavorite(@PathVariable(value = "slug") String slug) {
        userFavoriteBooksService.unfavorite(slug);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Book successfully removed from favorites!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
