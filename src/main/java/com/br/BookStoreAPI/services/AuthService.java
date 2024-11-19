package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginRequestDTO;
import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginResponseDTO;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.UserRepository;
import com.br.BookStoreAPI.repositories.UserVerifyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO loginDTO) {
        // Busca o usuário pelo e-mail
        var userOpt = userRepository.findByUserEmail(loginDTO.userEmail());

        // Verifica se o usuário existe e se a senha está correta
        if (userOpt.isEmpty() || !passwordEncoder.matches(loginDTO.password(), userOpt.get().getUserPassword())) {
            throw new BadCredentialsException("Invalid user email or password");
        }

        UserEntity user = userOpt.get();
        var now = Instant.now();
        var expiresIn = 300L; // Expiração do token em segundos
        var role = user.getRole();

        var claims = JwtClaimsSet.builder()
                .issuer("teste")
                .subject(String.valueOf(user.getUserId()))
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("authorization", List.of(role.getRole()))
                .issuedAt(now)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }
}
