package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.factories.UserFactory;
import com.br.BookStoreAPI.globalExceptions.UserAlreadyExistsException;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserRequestDTO;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserResponseDTO;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.models.entities.UserVerifierEntity;
import com.br.BookStoreAPI.repositories.RoleRepository;
import com.br.BookStoreAPI.repositories.UserRepository;
import com.br.BookStoreAPI.repositories.UserVerifyRepository;
import com.br.BookStoreAPI.utils.enums.RoleType;
import com.br.BookStoreAPI.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserVerifyRepository userVerifyRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserResponseDTO create(UserRequestDTO dto) {
        var role = roleRepository.findByRoleName(RoleType.USER.name());
        var userFromDb = userRepository.findByUserEmail(dto.userEmail());
        if (userFromDb.isPresent()) {
            throw new UserAlreadyExistsException("User's email " + dto.userEmail() + " is already in use.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserFirstName(dto.userFirstName());
        userEntity.setUserLastName(dto.userLastName());
        userEntity.setUserEmail(dto.userEmail());
        userEntity.setUserPassword(bCryptPasswordEncoder.encode(dto.userPassword()));
        userEntity.setRole(role);
        userEntity.setUserStatus(UserStatus.PENDING);
        UserEntity result = userRepository.save(userEntity);

        UserVerifierEntity verifier = new UserVerifierEntity();
        verifier.setUser(result);
        verifier.setUuid(UUID.randomUUID());
        verifier.setExpriation(Instant.now().plusSeconds(3600));
        userVerifyRepository.save(verifier);
        emailService.sendEmail(dto.userEmail(),
                "Código de confirmação:",
                "Seu código é: " + verifier.getUuid());
        return new UserResponseDTO(result);
    }

    public UserResponseDTO getUserById(Long userId) {
        UserEntity result = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserResponseDTO(result);
    }

    public List<UserDetailsResponseDTO> getAll(Pageable pageable) {
        UserEntity currentUser = getCurrentUser();
        var adminRole = roleRepository.findByRoleName(RoleType.ADMIN.name());
        if (!adminRole.equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource");
        }
        Page<UserEntity> users = userRepository.findAll(pageable);
        return users
                .stream()
                .map(UserFactory::CreateDetails)
                .collect(Collectors.toList());
    }

    public UserResponseDTO update(UserRequestDTO userRequestDTO, Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userEntity.setUserFirstName(userRequestDTO.userFirstName());
        userEntity.setUserLastName(userRequestDTO.userLastName());
        userEntity.setUserEmail(userRequestDTO.userEmail());
        userEntity.setUserPassword(bCryptPasswordEncoder.encode(userRequestDTO.userPassword()));

        UserEntity updatedUser = userRepository.save(userEntity);
        return new UserResponseDTO(updatedUser);
    }

    public boolean delete(Long userId) {
        Optional<UserEntity> result = userRepository.findById(userId);
        if (result.isEmpty()) return false;
        userRepository.delete(result.get());
        return true;
    }

    private UserEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        String currentUserEmail = auth.getName();
        return userRepository.findByUserEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String verifyUser(String uuid) {
        UserVerifierEntity userVerifier = userVerifyRepository.findByUuid(UUID.fromString(uuid)).get();
        if (userVerifier != null) {
            if (userVerifier.getExpriation().compareTo(Instant.now()) >= 0) {
                UserEntity user = userVerifier.getUser();
                user.setUserStatus(UserStatus.VERIFIED);
                userRepository.save(user);
                return "User verified";
            } else {
                userVerifyRepository.delete(userVerifier);
                UserEntity user = userVerifier.getUser();
                user.setUserStatus(UserStatus.UNVERIFIED);
                return "Tempo de verificação expirado";
            }
        }
        return uuid;
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
