package com.br.BookStoreAPI.controllers;

import com.br.BookStoreAPI.models.DTOs.errorsDTOs.ErrorResponseDTO;
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
import org.springframework.web.bind.annotation.*;

@RestController
@Getter
@Setter
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
            LoginResponseDTO reponse = authService.login(loginRequestDTO);
            return ResponseEntity.ok(reponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @GetMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        if (authService.validateToken(jwtToken)) {
            return ResponseEntity.ok("Token is valid! You can access other endpoints.");
        } else {
            return ResponseEntity.status(401).body("Invalid token.");
        }
    }
}
