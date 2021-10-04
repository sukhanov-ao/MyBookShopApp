package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.book.Author;
import com.example.mybookshopapp.data.book.AuthorSection;
import com.example.mybookshopapp.data.genre.Genre;
import com.example.mybookshopapp.data.genre.GenreSection;
import com.example.mybookshopapp.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    private List<Genre> getGenresData() {
        return genreRepository.findAll();
    }

    public List<GenreSection> getGenresSortByTypes() {
        List<Genre> genres = getGenresData();
        return genres.stream()
                .collect(Collectors.groupingBy(Genre::getGenreType))
                .entrySet().stream().map(genresSectionMap -> {
                    GenreSection genreSection = new GenreSection();
                    genreSection.setGenreType(genresSectionMap.getKey());
                    genreSection.setGenres(genresSectionMap.getValue());
                    return genreSection;
                }).collect(Collectors.toList());
    }
}
