package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginRequestDTO;
import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginResponseDTO;
import com.br.BookStoreAPI.models.entities.RoleEntity;
import com.br.BookStoreAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;

    public AuthService(UserRepository userRepository, JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder, @Qualifier("jwtDecoder") JwtDecoder jwtDecoder) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public LoginResponseDTO login(LoginRequestDTO loginDTO){
        var user = userRepository.findByUserEmail(loginDTO.userEmail());
        if (user.isEmpty() || !user.get().isLoginCorrect(loginDTO, passwordEncoder)) {
            throw new BadCredentialsException("Invalid username or password");
        }
        var now = Instant.now();
        var expiresIn = 300L;
        var scope = user.get().getRoles()
                .stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toList());
        var claims = JwtClaimsSet.builder()
                .issuer("teste")
                .subject(user.get().getUserEmail().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

    public boolean validateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
