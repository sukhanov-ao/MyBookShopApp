package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.annotations.BookStatusChangeable;
import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.repositories.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Controller
@RequestMapping("/books")
public class PostponedPageController {

    private final BookRepository bookRepository;

    public PostponedPageController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @ModelAttribute(name = "bookPostponed")
    public List<Book> bookPostponed() {
        return new ArrayList<>();
    }

    @GetMapping("/postponed")
    public String handlePostponedRequest(@CookieValue(value = "postponedContents", required = false) String postponedContents,
                                    Model model) {
        if (postponedContents == null || postponedContents.isEmpty()) {
            model.addAttribute("isPostponedEmpty", true);
        } else {
            model.addAttribute("isPostponedEmpty", false);
            postponedContents = postponedContents.startsWith("/") ? postponedContents.substring(1) : postponedContents;
            postponedContents = postponedContents.endsWith("/") ? postponedContents.substring(0, postponedContents.length() - 1) : postponedContents;
            String[] cookieSlug = postponedContents.split("/");
            List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlug);
            model.addAttribute("bookPostponed", booksFromCookieSlugs);
        }
        return "postponed";
    }

    @BookStatusChangeable
    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String handleRemoveBookFromPostponedRequest(@PathVariable("slug") String slug,
                                                  @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                                  HttpServletResponse response,
                                                  Model model) {

        if (postponedContents != null && !postponedContents.isEmpty()) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(postponedContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("postponedContents", String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);
        } else {
            model.addAttribute("isPostponedEmpty", true);
        }

        return "redirect:/books/postponed";
    }

    @BookStatusChangeable
    @PostMapping("/changeBookStatus/postponed/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                         HttpServletResponse response,
                                         Model model) {

        if (postponedContents == null || postponedContents.isEmpty()) {
            Cookie cookie = new Cookie("postponedContents", slug);
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);
        } else if (!postponedContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(postponedContents).add(slug);
            Cookie cookie = new Cookie("postponedContents", stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);
        }

        return "redirect:/books/" + slug;
    }
}
