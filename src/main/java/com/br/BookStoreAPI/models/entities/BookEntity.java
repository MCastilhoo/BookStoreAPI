package com.br.BookStoreAPI.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "BOOKS")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "BOOK_ID")
    private Long bookId;

    @NotBlank
    @Column(name = "TITLE")
    private String title;

    @NotNull
    @Column(name = "PAGE_NUMBERS")
    private int pageNumbers;

    @NotBlank
    @Column(name = "AUTHOR")
    private String author;

    @NotNull
    @Column(name = "PRICE")
    private float price;


    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "MODIFICATION_DATE")
    private LocalDateTime modificationDate;

    @PrePersist
    protected void onCreate() {creationDate = LocalDateTime.now();}

    @PreUpdate
    protected void onUpdate() {modificationDate = LocalDateTime.now();}
}
