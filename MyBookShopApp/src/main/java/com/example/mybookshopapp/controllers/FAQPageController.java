package com.example.mybookshopapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FAQPageController {

    @GetMapping("/faq")
    public String helpPage() {
        return "faq";
    }
}
