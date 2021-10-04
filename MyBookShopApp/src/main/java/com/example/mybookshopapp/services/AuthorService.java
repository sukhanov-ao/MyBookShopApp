package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.book.Author;
import com.example.mybookshopapp.repositories.AuthorRepository;
import com.example.mybookshopapp.data.book.AuthorSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private List<Author> getAuthorsData() {
        return authorRepository.findAllByOrderByLastNameAsc();
    }

    public List<AuthorSection> getAuthorsStartWithLetter() {
        List<Author> authors = getAuthorsData();
        return authors.stream()
                .collect(Collectors.groupingBy((Author author) -> author.getLastName().substring(0, 1).toUpperCase()))
                .entrySet().stream().map(authorSectionMap -> {
                    AuthorSection authorSection = new AuthorSection();
                    authorSection.setLetter(authorSectionMap.getKey());
                    authorSection.setAuthors(authorSectionMap.getValue());
                    return authorSection;
                }).collect(Collectors.toList());
    }

    public Author getAuthorById(Integer id) {
        return authorRepository.getById(id);
    }
}
