package com.br.BookStoreAPI.models.entities;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "BOOKS")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "BOOK_ID")
    private UUID bookId;

    @NotBlank
    @Column(name = "TITLE")
    private String bookName;

    @NotNull
    @Column(name = "PAGE_NUMBERS")
    private int pageNumbers;

    @NotBlank
    @Column(name = "AUTHOR")
    private String author;


    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "MODIFICATION_DATE")
    private LocalDateTime modificationDate;

    @PrePersist
    protected void onCreate() {creationDate = LocalDateTime.now();}

    @PreUpdate
    protected void onUpdate() {modificationDate = LocalDateTime.now();}
}
