package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BooksPageDTO;
import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

@Controller
public class RecentPageController {

    private final BookService bookService;
    private final Calendar calendar = Calendar.getInstance();


    @Autowired
    public RecentPageController(BookService bookService) {
        this.bookService = bookService;
        calendar.add(Calendar.MONTH, -1);
    }

    @ModelAttribute("mostRecentBooks")
    public List<Book> recentBooks() {
        return new ArrayList<>();
    }

    @GetMapping("/books/recent")
    public String recentBookPage(Model model) {
        Date dateTo = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTo);
        calendar.add(Calendar.MONTH, -1);
        Date dateFrom = calendar.getTime();
        model.addAttribute("mostRecentBooks", bookService.getMostRecentBooks(dateFrom, dateTo, 0, 20).getContent());
        return "books/recent";
    }

    @GetMapping("books/recent/page")
    @ResponseBody
    public BooksPageDTO getNextRecentPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @RequestParam("from") String from,
                                          @RequestParam("to") String to) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date dateFrom = new Date();
        Date dateTo = new Date();
        try {
             dateFrom = simpleDateFormat.parse(from);
             dateTo= simpleDateFormat.parse(to);
        } catch (ParseException e) {
            Logger.getLogger(this.getClass().getSimpleName()).warning(e.getLocalizedMessage());
        }
        return new BooksPageDTO(bookService.getMostRecentBooks(dateFrom, dateTo, offset, limit).getContent());
    }
}
