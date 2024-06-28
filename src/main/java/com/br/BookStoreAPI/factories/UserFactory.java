package com.br.BookStoreAPI.factories;

import com.br.BookStoreAPI.models.DTOs.userDTOs.UserDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.UserEntity;

public class UserFactory {
    public static UserDetailsResponseDTO CreateDetails(UserEntity userEntity){
        return  new UserDetailsResponseDTO(
                userEntity.getUserdId(),
                userEntity.getUserFirstName(),
                userEntity.getUserSecondName(),
                userEntity.getUserEmail(),
                userEntity.getUserPassword(),
                userEntity.getCreationDate(),
                userEntity.getModificationDate()
        );
    }
}
