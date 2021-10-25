package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.annotations.ApiExecutionTimeLoggable;
import com.example.mybookshopapp.data.BooksPageDTO;
import com.example.mybookshopapp.data.book.Author;
import com.example.mybookshopapp.data.book.AuthorSection;
import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.services.AuthorService;
import com.example.mybookshopapp.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Api(description = "authors data")
public class AuthorsPageController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorsPageController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @ModelAttribute("authorSectionData")
    public List<AuthorSection> getAuthorSection() {
        return authorService.getAuthorsStartWithLetter();
    }

    @ModelAttribute("author")
    public Author getAuthor() {
        return new Author();
    }

    @ModelAttribute("authorBooks")
    public List<Book> getAuthorBooks() {
        return new ArrayList<>();
    }

    @ApiExecutionTimeLoggable
    @GetMapping("/authors")
    public String getAuthorsStartsWithLetter() {
        return "/authors/index";
    }

    @ApiExecutionTimeLoggable
    @ApiOperation("method to get map of authors")
    @GetMapping("/api/authors")
    @ResponseBody
    public List<AuthorSection> authors() {
        return authorService.getAuthorsStartWithLetter();
    }

    @ApiExecutionTimeLoggable
    @GetMapping("/authors/slug/{id:\\d+}")
    public String slugPage(@PathVariable Integer id, Model model) {
        Page<Book> bookPage = bookService.getBooksByAuthorId(id, 0, 20);
        model.addAttribute("author", authorService.getAuthorById(id));
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("size", bookPage.getTotalElements());
        return "/authors/slug";
    }

    @ApiExecutionTimeLoggable
    @GetMapping("/books/author/{authorId:\\d+}")
    public String authorBooks(@PathVariable Integer authorId, Model model) {
        model.addAttribute("author", authorService.getAuthorById(authorId));
        model.addAttribute("authorBooks", bookService.getBooksByAuthorId(authorId, 0, 20).getContent());
        return "/books/author";
    }

    @ApiExecutionTimeLoggable
    @GetMapping("/books/author/page/{authorId:\\d+}")
    @ResponseBody
    public BooksPageDTO getNextPage(@RequestParam("offset") Integer offset,
                                    @RequestParam("limit") Integer limit,
                                    @PathVariable(value = "authorId", required = false) Integer authorId) {
        return new BooksPageDTO(bookService.getBooksByAuthorId(authorId, offset, limit).getContent());
    }
}
