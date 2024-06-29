package com.br.BookStoreAPI.controller;


import com.br.BookStoreAPI.models.DTOs.adminDTOs.AdminDetailsResponseDTO;
import com.br.BookStoreAPI.models.DTOs.adminDTOs.AdminRequestDTO;
import com.br.BookStoreAPI.models.DTOs.adminDTOs.AdminResponseDTO;
import com.br.BookStoreAPI.models.entities.AdminEntity;
import com.br.BookStoreAPI.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/api/admins")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminResponseDTO>create(@RequestBody @Valid AdminRequestDTO dto) {
        AdminResponseDTO responseDTO = adminService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDTO>findById(@PathVariable (value = "id") UUID id) {
        AdminResponseDTO result = adminService.getAdminById(id);
        if(result == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<AdminDetailsResponseDTO>>getAll(@PageableDefault(page = 0, size = 10) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllAdmins(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDTO>update(@PathVariable(value = "id")UUID id, @RequestBody @Valid AdminRequestDTO dto) {
      return ResponseEntity.status(HttpStatus.OK).body(adminService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AdminEntity> delete(@PathVariable(value = "id")UUID id){
        if(adminService.delete(id)){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
