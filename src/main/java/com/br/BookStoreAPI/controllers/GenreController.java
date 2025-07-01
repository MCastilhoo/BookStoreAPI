package com.br.BookStoreAPI.controllers;

import com.br.BookStoreAPI.models.DTOs.genreDTOs.GenreRequestDTO;
import com.br.BookStoreAPI.models.DTOs.genreDTOs.GenreResponseDTO;
import com.br.BookStoreAPI.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    @Autowired
    private GenreService service;

    @PostMapping
    public ResponseEntity<GenreResponseDTO> createGenre(@RequestBody GenreRequestDTO dto){
        try {
            GenreResponseDTO genreResponseDTO = service.createGenre(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(genreResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public List<GenreResponseDTO> getAllGenres(){
        return service.getAllGenres();
    }
}
