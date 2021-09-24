package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookshop")
public class AuthorsPageController {

    private final AuthorService authorService;

    @Autowired
    public AuthorsPageController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors/")
    public String getAuthorsStartsWithLetter(Model model) {
        model.addAttribute("authorSectionData", authorService.getAuthorsStartWithLetter());
        return "authors/index";
    }

}
