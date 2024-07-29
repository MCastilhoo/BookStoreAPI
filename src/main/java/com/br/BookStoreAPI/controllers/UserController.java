package com.br.BookStoreAPI.controllers;


import com.br.BookStoreAPI.models.DTOs.userDTOs.UserDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserRequestDTO;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserResponseDTO;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.services.UserService;
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
@RequestMapping("/rest/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponseDTO>create(@RequestBody UserRequestDTO dto)  {
        UserResponseDTO responseDTO = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO>get(@PathVariable(value = "id") UUID id) {
        UserResponseDTO result = userService.getUserById(id);

        if(result == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<List<UserDetailsResponseDTO>> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserEntity> delete(@PathVariable(value = "id") UUID id) {
        if(userService.delete(id)){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
