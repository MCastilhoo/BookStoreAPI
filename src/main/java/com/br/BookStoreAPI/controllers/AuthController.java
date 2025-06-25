package com.br.BookStoreAPI.controllers;

import com.br.BookStoreAPI.exceptions.GlobalExceptionHandler;
import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginRequestDTO;
import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginResponseDTO;
import com.br.BookStoreAPI.services.AuthService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@NoArgsConstructor(force = true)
public class AuthController {
    @Autowired
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO response = authService.login(loginRequestDTO);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GlobalExceptionHandler());
        }
    }
}
