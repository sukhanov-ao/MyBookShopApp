package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.Author;
import com.example.mybookshopapp.data.AuthorSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private List<Author> getAuthorsData() {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors", (ResultSet rs, int rowNum) -> {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setFirstname(rs.getString("first_name"));
            author.setLastname(rs.getString("last_name"));
            return author;
        });
        return authors.stream().sorted(Comparator.comparing(Author::getLastname)).collect(Collectors.toList());
    }

    public List<AuthorSection> getAuthorsStartWithLetter() {
        List<Author> authors = getAuthorsData();
        List<AuthorSection> authorSections = new ArrayList<>();

        authors.stream()
                .collect(Collectors.groupingBy((Author author) -> author.getLastname().substring(0, 1).toUpperCase()))
                .entrySet().stream().map(authorSectionMap -> {
            AuthorSection authorSection = new AuthorSection();
            authorSection.setLetter(authorSectionMap.getKey());
            authorSection.setAuthors(authorSectionMap.getValue());
            authorSections.add(authorSection);
            return authorSection;
        }).collect(Collectors.toList());
        return authorSections;
    }

    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors", (ResultSet rs, int rowNum) -> {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setFirstname(rs.getString("first_name"));
            author.setLastname(rs.getString("last_name"));
            return author;
        });

        return authors.stream()
                .collect(Collectors.groupingBy((Author author) -> author.getLastname().substring(0, 1).toUpperCase()));
    }
}
