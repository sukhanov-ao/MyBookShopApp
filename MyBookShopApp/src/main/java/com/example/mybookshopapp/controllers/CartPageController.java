package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.annotations.BookStatusChangeable;
import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Controller
@RequestMapping("/books")
public class CartPageController {

    private final BookRepository bookRepository;
    private final PaymentService paymentService;

    @Autowired
    public CartPageController(BookRepository bookRepository, PaymentService paymentService) {
        this.bookRepository = bookRepository;
        this.paymentService = paymentService;
    }


    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        if (cartContents == null || cartContents.isEmpty()) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
            String[] cookieSlug = cartContents.split("/");
            List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlug);
            model.addAttribute("bookCart", booksFromCookieSlugs);
        }
        return "cart";
    }

    @BookStatusChangeable
    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug,
                                                  @CookieValue(name = "cartContents", required = false) String cartContents,
                                                  HttpServletResponse response,
                                                  Model model) {

        if (cartContents != null && !cartContents.isEmpty()) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", true);
        }

        return "redirect:/books/cart";
    }

    @BookStatusChangeable
    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         @CookieValue(name = "cartContents", required = false) String cartContents,
                                         HttpServletResponse response,
                                         Model model) {

        if (cartContents == null || cartContents.isEmpty()) {
            Cookie cookie = new Cookie("cartContents", slug);
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else if (!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        }

        return "redirect:/books/" + slug;
    }

    @GetMapping("/pay")
    public RedirectView handlePay(@CookieValue(name = "cartContents", required = false) String cartContents) throws NoSuchAlgorithmException {
        cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
        cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
        String[] cookieSlug = cartContents.split("/");
        List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlug);
        String paymentUrl = paymentService.getPaymentUrl(booksFromCookieSlugs);
        return new RedirectView(paymentUrl);
    }


    @GetMapping("http://127.0.0.1:8085/payment/accept/*")
    public String getAcceptPayment() {
        return "redirect:/payment/accept";
    }

    @GetMapping("/payment/accept")
    public String handlePaymentAccept(@RequestParam("OutSum") Double outSum,
                                     @RequestParam("InvId") String invId,
                                     @RequestParam("SignatureValue") String signatureValue,
                                     @RequestParam("IsTest") String isTest,
                                     @RequestParam("Culture") String culture) {


        return "redirect:/profile";
    }

}
