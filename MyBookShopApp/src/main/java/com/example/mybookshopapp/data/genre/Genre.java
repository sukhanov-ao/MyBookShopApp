package com.example.mybookshopapp.data.genre;

import com.example.mybookshopapp.data.book.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @ManyToOne
    @JoinColumn(name = "genre_type_id", referencedColumnName = "id", columnDefinition = "INT")
    @JsonIgnore
    private GenreType genreType;

    @OneToMany(mappedBy = "genre")
    private List<Book> bookListByGenre = new ArrayList<>();

    public GenreType getGenreType() {
        return genreType;
    }

    public List<Book> getBookListByGenre() {
        return bookListByGenre;
    }

    public void setBookListByGenre(List<Book> bookListByGenre) {
        this.bookListByGenre = bookListByGenre;
    }

    public void setGenreType(GenreType genreType) {
        this.genreType = genreType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
