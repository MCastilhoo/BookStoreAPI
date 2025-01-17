package com.br.BookStoreAPI.services;

import com.br.BookStoreAPI.factories.UserFactory;
import com.br.BookStoreAPI.exceptions.CustomException;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserRequestDTO;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserResponseDTO;
import com.br.BookStoreAPI.models.entities.RoleEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.models.entities.UserVerifierEntity;
import com.br.BookStoreAPI.repositories.RoleRepository;
import com.br.BookStoreAPI.repositories.UserRepository;
import com.br.BookStoreAPI.repositories.UserVerifyRepository;
import com.br.BookStoreAPI.utils.enums.RoleType;
import com.br.BookStoreAPI.utils.enums.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserVerifyRepository userVerifyRepository;
    private final EmailService emailService;
    private final PasswordEncoder bCryptPasswordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserVerifyRepository userVerifyRepository,
            EmailService emailService,
            PasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userVerifyRepository = userVerifyRepository;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO create(UserRequestDTO dto) {
        var role = roleRepository.findByRoleName(RoleType.USER.name());

        // Verificar se o usuário já existe
        if (userRepository.findByUserEmail(dto.userEmail()).isPresent()) {
            throw new IllegalArgumentException("User's email " + dto.userEmail() + " is already in use.");
        }

        // Validar a requisição (como validar a senha, etc)
        validateUserRequest(dto);

        // Preparar a entidade do usuário
        UserEntity userEntity = prepareUserEntity(dto, role);

        // Salvar o usuário no banco de dados
        UserEntity savedUser = userRepository.save(userEntity);

        // Assincronamente, você pode enviar o e-mail ou realizar outras ações
        CompletableFuture.runAsync(() -> {
            try {
                UserVerifierEntity verifier = createUserVerifier(savedUser);
                userVerifyRepository.save(verifier);
                String verificationUrl = "http://localhost:8080/api/users/authenticate/" + verifier.getUuid();
                emailService.sendEmail(dto.userEmail(), "Verificação de Conta", buildVerificationEmail(dto.userFirstName(), verificationUrl));
            } catch (Exception e) {
                logger.error("Erro no processamento assíncrono de criação de usuário", e);
            }
        });

        // Retornar a resposta com o usuário salvo
        return new UserResponseDTO(savedUser);
    }


    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }

//    private void validateUserPassword(UserRequestDTO dto) {
//        if (!isValidPassword(dto.userPassword())){
//            throw new InvalidPasswordExcpetion("teste");
//        }
//    }

    private void validateUserRequest(UserRequestDTO dto) {
        if (dto.userFirstName() == null || dto.userFirstName().length() < 2) {
            throw new IllegalArgumentException("Nome inválido");
        }
        if (dto.userPassword() == null || dto.userPassword().length() < 8) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 8 caracteres");
        }
    }

    private UserEntity prepareUserEntity(UserRequestDTO dto, RoleEntity role) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserFirstName(dto.userFirstName());
        userEntity.setUserLastName(dto.userLastName());
        userEntity.setUserEmail(dto.userEmail());
        userEntity.setUserPassword(bCryptPasswordEncoder.encode(dto.userPassword()));
        userEntity.setRole(role);
        userEntity.setUserStatus(UserStatus.PENDING);
        return userEntity;
    }

    private UserVerifierEntity createUserVerifier(UserEntity user) {
        UserVerifierEntity verifier = new UserVerifierEntity();
        verifier.setUser(user);
        verifier.setUuid(UUID.randomUUID());
        verifier.setExpriation(Instant.now().plusSeconds(3600));
        return verifier;
    }

    private String buildVerificationEmail(String firstName, String verificationUrl) {
        return "Olá, " + firstName + ",\n\n" +
                "Por favor, clique no link abaixo para verificar sua conta:\n" +
                verificationUrl + "\n\n";
    }

    public UserResponseDTO getUserById(Long userId) {
        UserEntity result = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserResponseDTO(result);
    }

    public Page<UserDetailsResponseDTO> getAll(Pageable pageable) {
        UserEntity currentUser = getCurrentUser();
        var adminRole = roleRepository.findByRoleName(RoleType.ADMIN.name());

        if (!currentUser.getRole().equals(adminRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource");
        }

        Page<UserEntity> users = userRepository.findAll(pageable);
        return users.map(UserFactory::CreateDetails);
    }

    @Transactional
    public UserResponseDTO update(UserRequestDTO userRequestDTO, Long userId) {
        // Verificar se usuário existe
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Verificar se novo email já está em uso
        Optional<UserEntity> existingUser = userRepository.findByUserEmail(userRequestDTO.userEmail());
        if (existingUser.isPresent() && !existingUser.get().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Validar request
        validateUserRequest(userRequestDTO);

        // Atualizar campos
        userEntity.setUserFirstName(userRequestDTO.userFirstName());
        userEntity.setUserLastName(userRequestDTO.userLastName());
        userEntity.setUserEmail(userRequestDTO.userEmail());
        userEntity.setUserPassword(bCryptPasswordEncoder.encode(userRequestDTO.userPassword()));

        UserEntity updatedUser = userRepository.save(userEntity);
        return new UserResponseDTO(updatedUser);
    }

    @Transactional
    public boolean delete(Long userId) {
        UserEntity currentUser = getCurrentUser();
        var adminRole = roleRepository.findByRoleName(RoleType.ADMIN.name());

        // Verificar permissões
        if (!currentUser.getRole().equals(adminRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete users");
        }

        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    private UserEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        String currentUserEmail = auth.getName();
        return userRepository.findByUserEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional
    public String verifyUser(String uuid) {
        UserVerifierEntity userVerifier = userVerifyRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Verification not found"));

        if (userVerifier.getExpriation().compareTo(Instant.now()) >= 0) {
            UserEntity user = userVerifier.getUser();
            user.setUserStatus(UserStatus.VERIFIED);
            userRepository.save(user);
            userVerifyRepository.delete(userVerifier);
            return "User verified";
        } else {
            UserEntity user = userVerifier.getUser();
            user.setUserStatus(UserStatus.UNVERIFIED);
            userRepository.save(user);
            userVerifyRepository.delete(userVerifier);
            return "Tempo de verificação expirado";
        }
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}