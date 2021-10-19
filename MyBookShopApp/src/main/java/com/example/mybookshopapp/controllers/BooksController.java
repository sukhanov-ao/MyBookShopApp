package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BookReviewLikeValue;
import com.example.mybookshopapp.data.ResourceStorage;
import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.data.book.rating.BookRating;
import com.example.mybookshopapp.data.book.review.BookReview;
import com.example.mybookshopapp.repositories.BookRepository;
import com.example.mybookshopapp.repositories.RatingRepository;
import com.example.mybookshopapp.security.BookStoreUserDetails;
import com.example.mybookshopapp.services.BookReviewLikeService;
import com.example.mybookshopapp.services.BookReviewService;
import com.example.mybookshopapp.services.RatingService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookRepository bookRepository;
    private final ResourceStorage storage;
    private final RatingRepository ratingRepository;
    private final RatingService ratingService;
    private final BookReviewService bookReviewService;
    private final BookReviewLikeService reviewLikeService;
    private static final String REDIRECT_TO_BOOKS = "redirect:/books/";
    @Autowired
    public BooksController(BookRepository bookRepository, ResourceStorage storage, RatingRepository ratingRepository, RatingService ratingService, BookReviewService bookReviewService, BookReviewLikeService reviewLikeService) {
        this.bookRepository = bookRepository;
        this.storage = storage;
        this.ratingRepository = ratingRepository;
        this.ratingService = ratingService;
        this.bookReviewService = bookReviewService;
        this.reviewLikeService = reviewLikeService;
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file,
                                   @PathVariable("slug") String slug) throws IOException {

        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookRepository.findBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);
        return (REDIRECT_TO_BOOKS + slug);
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {

        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data length: " + data.length);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        Book book = bookRepository.findBookBySlug(slug);
        BookRating bookRating = ratingRepository.getRatingByBookId(book.getId());
        Integer averageRating = bookRating.getAverageRating();
        Integer votedAmount = bookRating.getAllVoteAmount();
        model.addAttribute("slugBook", book);
        model.addAttribute("bookRating", bookRating);
        model.addAttribute("bookAverageRating", averageRating);
        model.addAttribute("votedAmount", votedAmount);
        return "/books/slug";
    }

    @PostMapping("/changeBookStatus/vote/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         @RequestBody BookReviewLikeValue reviewLikeValue) {
        Book book = bookRepository.findBookBySlug(slug);
        BookRating bookRating = ratingRepository.getRatingByBookId(book.getId());
        bookRating.setStarByValue(reviewLikeValue.getValue());
        ratingService.save(bookRating);
        Logger.getLogger(this.getClass().getSimpleName()).info("Выполняем обновление рейтинга" + bookRating);
        return REDIRECT_TO_BOOKS + slug;
    }

    @PostMapping("/bookReview/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug,
                                         @RequestParam("userName") String userName,
                                         @RequestParam("comment") String text,
                                         @RequestParam("rating") Integer rating) {

        Book book = bookRepository.findBookBySlug(slug);
        BookReview review = new BookReview();
        review.setUserName(userName);
        review.setTime(LocalDateTime.now());
        review.setBook(book);
        review.setText(text);
        review.setRating(rating);
        bookReviewService.saveReview(review);

        return REDIRECT_TO_BOOKS + slug;
    }

    @PostMapping("/rateBookReview/{bookSlug}")
    public String handleBookReviewRateChanging(@RequestBody BookReviewLikeValue reviewLikeValue,
                                               @PathVariable("bookSlug") String bookSlug,
                                               @AuthenticationPrincipal BookStoreUserDetails user) {
        if (user != null) {
            reviewLikeService.saveReviewLike(user.getBookStoreUser(), reviewLikeValue.getReviewId(), reviewLikeValue.getValue());
        }

        return REDIRECT_TO_BOOKS + bookSlug;
    }
}
