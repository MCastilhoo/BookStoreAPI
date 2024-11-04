package com.br.BookStoreAPI.repositories;


import com.br.BookStoreAPI.models.entities.UserVerifierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserVerifyRepository extends JpaRepository <UserVerifierEntity, Long>{
    public Optional<UserVerifierEntity> findByUuid(UUID uuid);
    ;

}
