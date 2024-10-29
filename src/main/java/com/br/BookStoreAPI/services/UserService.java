package com.br.BookStoreAPI.services;


import com.br.BookStoreAPI.globalExceptions.UserAlreadyExistsException;
import com.br.BookStoreAPI.factories.UserFactory;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserRequestDTO;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserResponseDTO;
import com.br.BookStoreAPI.models.entities.RoleEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.RoleRepository;
import com.br.BookStoreAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }



    public UserResponseDTO create(UserRequestDTO dto) {
        var role = roleRepository.findByRoleName(RoleEntity.RoleType.USER.name());
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }

        // Verifica se o usuário já existe pelo email
        var userFromDb = userRepository.findByUserEmail(dto.userEmail());
        if (userFromDb.isPresent()) {
            throw new UserAlreadyExistsException("Email already in use: " + dto.userEmail());
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserFirstName(dto.userFirstName());
        userEntity.setUserLastName(dto.userLastName());
        userEntity.setUserEmail(dto.userEmail());
        userEntity.setUserPassword(bCryptPasswordEncoder.encode(dto.userPassword()));
        userEntity.setRoles(Set.of(role));

        UserEntity result = userRepository.save(userEntity);

        return new UserResponseDTO(result);
    }


    public UserResponseDTO getUserById(Long userId) {
        UserEntity result = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserResponseDTO(result);
    }

    public List<UserDetailsResponseDTO> getAll(Pageable pageable) {
        Page<UserEntity> users = userRepository.findAll(pageable);

        List<UserDetailsResponseDTO> results = users
                .stream()
                .map(UserFactory::CreateDetails)
                .collect(Collectors.toList());

        return results;
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

    public Optional<UserEntity> getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    public RoleEntity getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
