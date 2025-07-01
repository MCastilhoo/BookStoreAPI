package com.br.BookStoreAPI.config;

import com.br.BookStoreAPI.models.entities.GenreEntity;
import com.br.BookStoreAPI.repositories.GenreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class GenreConfig implements CommandLineRunner {
    private final GenreRepository genreRepository;

    public GenreConfig(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (genreRepository.count() == 0) {
            List<String>genres = List.of(
                    "Aventura",
                    "Ação"
            );
            genres.forEach(genre -> {
                if (genreRepository.findByGenre(genre).isEmpty()) {
                    genreRepository.save(new GenreEntity(null, genre));
                }
            });
            System.out.println("Generos inseridos com sucesso!");
        }
    }
}
