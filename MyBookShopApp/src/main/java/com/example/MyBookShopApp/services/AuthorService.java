package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.AuthorSection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final JdbcTemplate jdbcTemplate;

    public AuthorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private List<Author> getAuthorsData() {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors", (ResultSet rs, int rowNum) -> {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setFirstname(rs.getString("firstname"));
            author.setLastname(rs.getString("lastname"));
            return author;
        });
        return authors.stream().sorted(Comparator.comparing(Author::getLastname)).collect(Collectors.toList());
    }

    public List<AuthorSection> getAuthorsStartWithLetter() {
        List<Author> authors = getAuthorsData();
        List<AuthorSection> authorSections = new ArrayList<>();

        authors.stream()
                .collect(Collectors.groupingBy((Author author) -> author.getLastname().substring(0, 1)))
                .forEach((key, value) -> {
                    AuthorSection authorSection = new AuthorSection();
                    authorSection.setLetter(key);
                    authorSection.setAuthors(value);
                    authorSections.add(authorSection);
                });
        return authorSections;
    }
}
