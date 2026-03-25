package com.br.BookStoreAPI.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "USER_FAVORITE_BOOK")
public class UserFavoriteBooksEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userFavoriteBookId;

    @ManyToOne
    @JoinColumn(name = "BOOK")
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "USER")
    private UserEntity user;

    @Column(name = "FAVORITE_DATE")
    private LocalDateTime favoriteDate;

    @PrePersist
    protected void onCreate() {favoriteDate = LocalDateTime.now();}
}



