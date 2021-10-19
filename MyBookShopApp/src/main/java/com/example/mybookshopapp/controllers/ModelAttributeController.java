package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.security.BookStoreUser;
import com.example.mybookshopapp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ModelAttributeController {

    private final BookRepository bookRepository;
    private final BookStoreUserRegister userRegister;

    public ModelAttributeController(BookRepository bookRepository, BookStoreUserRegister userRegister) {
        this.bookRepository = bookRepository;
        this.userRegister = userRegister;
    }

    @ModelAttribute(name = "bookInPostponedAmount")
    public Integer bookInPostponedAmount(@CookieValue(value = "postponedContents", required = false) String postponedContents) {
        if (postponedContents == null || postponedContents.isEmpty()) {
            return 0;
        } else {
            postponedContents = postponedContents.startsWith("/") ? postponedContents.substring(1) : postponedContents;
            postponedContents = postponedContents.endsWith("/") ? postponedContents.substring(0, postponedContents.length() - 1) : postponedContents;
            String[] cookieSlug = postponedContents.split("/");
            List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlug);
            return booksFromCookieSlugs.size();
        }
    }

    @ModelAttribute(name = "bookInCartAmount")
    public Integer bookInCartAmount(@CookieValue(value = "cartContents", required = false) String cartContents) {
        if (cartContents == null || cartContents.isEmpty()) {
            return 0;
        } else {
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
            String[] cookieSlug = cartContents.split("/");
            List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlug);
            return booksFromCookieSlugs.size();
        }
    }
}
