package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginRequestDTO;
import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginResponseDTO;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.UserRepository;
import com.br.BookStoreAPI.repositories.UserVerifyRepository;
import com.br.BookStoreAPI.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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

        var userOpt = userRepository.findByUserEmail(loginDTO.userEmail());
        if (userOpt.isEmpty() || !passwordEncoder.matches(loginDTO.password(), userOpt.get().getUserPassword())) {
            throw new BadCredentialsException("Invalid user email or password");
        }

        UserEntity user = userOpt.get();
        var now = Instant.now();
        var expiresIn = 300L;
        var role = user.getRole();

        if (user.getUserStatus() != UserStatus.VERIFIED) {
            throw new DisabledException("User not authenticated");
        }

        var claims = JwtClaimsSet.builder()
                .issuer("teste")
                .subject(String.valueOf(user.getUserEmail()))
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("authorization", List.of(role.getRole()))
                .claim("firstName", user.getUserFirstName())
                .claim("lastName",  user.getUserLastName())
                .claim("email", user.getUserEmail())

                .issuedAt(now)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }
}
