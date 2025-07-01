package com.br.BookStoreAPI.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "GENRES")
public class GenreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GENRE_ID")
    private Integer genreId;

    @NotBlank
    @Column(name = "GENRE")
    private String genre;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    private Set<BookEntity> books;

    public GenreEntity( Long genreId, String genre) {
        this.genre = genre;
    }
}
