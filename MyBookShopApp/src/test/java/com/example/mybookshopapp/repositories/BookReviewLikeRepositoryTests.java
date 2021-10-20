package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.book.Author;
import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.data.book.review.BookReview;
import com.example.mybookshopapp.data.book.review.BookReviewLike;
import com.example.mybookshopapp.security.BookStoreUser;
import com.example.mybookshopapp.security.BookStoreUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookReviewLikeRepositoryTests {

    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookStoreUserRepository bookStoreUserRepository;

    @Autowired
    public BookReviewLikeRepositoryTests(BookReviewLikeRepository bookReviewLikeRepository, BookRepository bookRepository, AuthorRepository authorRepository, BookReviewRepository bookReviewRepository, BookStoreUserRepository bookStoreUserRepository) {
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookStoreUserRepository = bookStoreUserRepository;
    }

    @Test
    void getBookReviewLikeByUserAndBookReview() {
        BookStoreUser bookStoreUser = bookStoreUserRepository.findBookStoreUserByEmail("test5@gmail.com");
        BookReview bookReview = bookReviewRepository.findBookReviewById(1);
        BookReviewLike bookReviewLike = bookReviewLikeRepository.findBookReviewLikeByUserAndBookReview(bookStoreUser, bookReview);

        assertNotNull(bookReviewLike);
    }
}