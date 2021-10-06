package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BooksPageDTO;
import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.services.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PopularPageController {

    private final BookService bookService;

    public PopularPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("mostPopularBooks")
    public List<Book> recentBooks() {
        return new ArrayList<>();
    }

    @GetMapping("/books/popular")
    public String recentBookPage(Model model) {
        model.addAttribute("mostPopularBooks", bookService.getMostPopularBooks(0, 20).getContent());
        return "books/popular";
    }

    @GetMapping("books/popular/page")
    @ResponseBody
    public BooksPageDTO getNextRecentPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit) {
        return new BooksPageDTO(bookService.getMostPopularBooks(offset, limit).getContent());
    }
}
