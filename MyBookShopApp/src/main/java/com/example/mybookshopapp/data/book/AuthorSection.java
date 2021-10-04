package com.example.mybookshopapp.data.book;

import com.example.mybookshopapp.data.book.Author;

import java.util.List;

public class AuthorSection {

    String letter;
    List<Author> authors;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "AuthorSection{" +
                "letter='" + letter + '\'' +
                ", authors=" + authors +
                '}';
    }
}
