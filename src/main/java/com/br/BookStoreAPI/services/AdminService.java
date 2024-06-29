package com.br.BookStoreAPI.services;


import com.br.BookStoreAPI.factories.AdminFactory;
import com.br.BookStoreAPI.models.DTOs.adminDTOs.AdminDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.adminDTOs.AdminRequestDTO;
import com.br.BookStoreAPI.models.DTOs.adminDTOs.AdminResponseDTO;
import com.br.BookStoreAPI.models.entities.AdminEntity;
import com.br.BookStoreAPI.repositories.AdminRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {this.adminRepository = adminRepository;}

    public AdminResponseDTO create(AdminRequestDTO adminRequestDTO) {
        AdminEntity adminEntity = new AdminEntity();
        BeanUtils.copyProperties(adminRequestDTO, adminEntity);

        AdminEntity result = adminRepository.save(adminEntity);

        return new AdminResponseDTO(result);
    }

    public AdminResponseDTO getAdminById(UUID adminId) {
        Optional<AdminEntity> result = adminRepository.findById(adminId);
        if(result.isEmpty()) return null;
        return new AdminResponseDTO(result.get());
    }

    public List<AdminDetailsResponseDTO> getAllAdmins(Pageable pageable) {
        Page<AdminEntity> admins = adminRepository.findAll(pageable);

        List<AdminDetailsResponseDTO> results = admins
                .stream()
                .map(AdminFactory::CreateDetails)
                .collect(Collectors.toList());

        return results;
    }

    public AdminResponseDTO update( AdminRequestDTO adminRequestDTO, UUID adminId) {
        Optional<AdminEntity> result = adminRepository.findById(adminId);

        if(result.isEmpty()) return null;

        result.get().setAdministratorFirstName(adminRequestDTO.administratorFirstName());
        result.get().setAdministratorLastName(adminRequestDTO.administatorLastName());
        result.get().setAdministratorEmail(adminRequestDTO.administratorEmail());
        result.get().setAdministratorPassword(adminRequestDTO.administratorPassword());

        AdminEntity adminEntity = adminRepository.save(result.get());

        return new AdminResponseDTO(adminEntity);
    }

    public boolean delete(UUID adminId) {
        Optional<AdminEntity> result = adminRepository.findById(adminId);
        if(result.isEmpty()) return false;
        adminRepository.delete(result.get());
        return true;
    }
}
