package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BooksPageDTO;
import com.example.mybookshopapp.data.genre.Genre;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GenresPageController {

    private final GenreService genreService;
    private final BookService bookService;

    @Autowired
    public GenresPageController(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @ModelAttribute("genres")
    public List<Genre> getAllGenres() {
        return genreService.findAll();
    }

    @GetMapping("/genres")
    public String genres() {
        return "genres/index";
    }

    @GetMapping("/genres/{genre}")
    public String getGenrePage(@PathVariable Genre genre,
                               Model model) {
        model.addAttribute("genres", genre);
        model.addAttribute("genreBooks", bookService.getPageBookByGenre(genre, 0, 20).getContent());
        return "genres/slug";
    }

    @GetMapping("/books/genre/{genreId}")
    @ResponseBody
    public BooksPageDTO getNextSearchPageGenre(@PathVariable Integer genreId,
                                               @RequestParam("offset") Integer offset,
                                               @RequestParam("limit") Integer limit) {
        return new BooksPageDTO(bookService.getPageBookByGenreId(genreId, offset, limit).getContent());
    }
}
