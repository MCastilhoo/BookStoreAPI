package com.br.BookStoreAPI.controllers;

import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginRequestDTO;
import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginResponseDTO;
import com.br.BookStoreAPI.repositories.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@Getter
@Setter
@NoArgsConstructor(force = true)
public class TokenController {
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public TokenController(JwtEncoder jwtEncoder,
                           UserRepository userRepository,
                           PasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        var user = userRepository.findByUserEmail(loginRequestDTO.userEmail());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequestDTO, passwordEncoder)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        var now = Instant.now();
        var expiresIn = 300L;
        var claims = JwtClaimsSet.builder()
                .issuer("teste")
                .subject(user.get().getUserdId().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponseDTO(jwtValue, expiresIn));
    }
}
