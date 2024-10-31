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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @NotBlank
    @Column(name = "CATEGORY")
    private String category;

    @NotBlank
    @Column(name = "QUANTITY")
    private Integer quantity;

    @NotNull
    @Column(name = "PRICE")
    private Float price;


    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "MODIFICATION_DATE")
    private LocalDateTime modificationDate;

    @PrePersist
    protected void onCreate() {creationDate = LocalDateTime.now();}

    @PreUpdate
    protected void onUpdate() {modificationDate = LocalDateTime.now();}
}
