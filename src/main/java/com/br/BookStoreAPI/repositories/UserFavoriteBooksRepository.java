package com.br.BookStoreAPI.repositories;

import com.br.BookStoreAPI.models.entities.UserFavoriteBooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoriteBooksRepository extends JpaRepository<UserFavoriteBooksEntity, Long> {
    boolean existsByUserUserIdAndBookSlug(Long userIde, String slug);

    void deleteByUserUserIdAndBookSlug(Long userId, String slug);
}
