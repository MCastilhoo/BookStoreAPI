package com.br.BookStoreAPI.services;


import com.br.BookStoreAPI.factories.UserFactory;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserRequestDTO;
import com.br.BookStoreAPI.models.DTOs.userDTOs.UserResponseDTO;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {this.userRepository = userRepository;}

    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userRequestDTO, userEntity);

        UserEntity result = userRepository.save(userEntity);

        return new UserResponseDTO(result);
    }

    public UserResponseDTO getUserById(UUID userId) {
        Optional<UserEntity> result = userRepository.findById(userId);
        if(result.isEmpty()) return null;
        return new UserResponseDTO(result.get());
    }

    public List<UserDetailsResponseDTO> getAll(Pageable pageable) {
        Page<UserEntity> users = userRepository.findAll(pageable);

        List<UserDetailsResponseDTO> results = users
                .stream()
                .map(UserFactory::CreateDetails)
                .collect(Collectors.toList());

        return results;
    }

    public UserResponseDTO update( UserRequestDTO userRequestDTO, UUID userId) {
        Optional<UserEntity> result = userRepository.findById(userId);

        if(result.isEmpty()) return null;

        result.get().setUserFirstName(userRequestDTO.userFirstName());
        result.get().setUserLastName(userRequestDTO.userLastName());
        result.get().setUserEmail(userRequestDTO.userEmail());
        result.get().setUserPassword(userRequestDTO.userPassword());

        UserEntity userEntity = userRepository.save(result.get());

        return new UserResponseDTO(userEntity);
    }

    public boolean delete(UUID userId) {
        Optional<UserEntity> result = userRepository.findById(userId);
        if(result.isEmpty()) return false;
        userRepository.delete(result.get());
        return true;
    }
}
