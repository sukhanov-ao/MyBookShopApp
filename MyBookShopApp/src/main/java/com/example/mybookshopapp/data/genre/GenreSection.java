package com.example.mybookshopapp.data.genre;

import java.util.List;

public class GenreSection {

    private GenreType genreType;
    private List<Genre> genres;

    public GenreType getGenreType() {
        return genreType;
    }

    public void setGenreType(GenreType genreType) {
        this.genreType = genreType;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
