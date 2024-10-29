package com.br.BookStoreAPI.controllers;


import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookRequestDTO;
import com.br.BookStoreAPI.models.DTOs.bookDTOs.BookResponseDTO;
import com.br.BookStoreAPI.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping()
    public ResponseEntity<BookResponseDTO> create(@RequestBody @Valid BookRequestDTO dto) {
        BookResponseDTO bookResponseDTO = bookService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable Long id) {
        BookResponseDTO responseDTO = bookService.getBookById(id);
        if(responseDTO == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<BookDetailsResponseDTO>> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable(value = "id") Long id, @RequestBody @Valid BookRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.update(dto, id));
    }

    @DeleteMapping
    public ResponseEntity<BookResponseDTO> delete(@PathVariable (value = "id") Long id) {
        if(bookService.delete(id)){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
