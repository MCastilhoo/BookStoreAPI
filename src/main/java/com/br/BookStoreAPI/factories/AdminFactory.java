package com.br.BookStoreAPI.factories;

import com.br.BookStoreAPI.models.DTOs.adminDTOs.AdminDetailsResponseDTO;
import com.br.BookStoreAPI.models.entities.AdminEntity;

public class AdminFactory {
    public static AdminDetailsResponseDTO CreateDetails(AdminEntity adminEntity) {
        return new AdminDetailsResponseDTO(
                adminEntity.getAdminId(),
                adminEntity.getAdministratorFirstName(),
                adminEntity.getAdministratorLastName(),
                adminEntity.getAdministratorEmail(),
                adminEntity.getAdministratorPassword(),
                adminEntity.getCreationDate(),
                adminEntity.getModificationDate()
        );
    }
}
