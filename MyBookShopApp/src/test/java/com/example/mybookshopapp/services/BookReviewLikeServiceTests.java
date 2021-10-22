package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.book.review.BookReview;
import com.example.mybookshopapp.data.book.review.BookReviewLike;
import com.example.mybookshopapp.repositories.BookReviewLikeRepository;
import com.example.mybookshopapp.repositories.BookReviewRepository;
import com.example.mybookshopapp.security.BookStoreUser;
import com.example.mybookshopapp.security.BookStoreUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookReviewLikeServiceTests {

    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookStoreUserRepository bookStoreUserRepository;
    private final BookReviewLikeService bookReviewLikeService;

    @Autowired
    public BookReviewLikeServiceTests(BookReviewLikeRepository bookReviewLikeRepository, BookReviewRepository bookReviewRepository, BookStoreUserRepository bookStoreUserRepository, BookReviewLikeService bookReviewLikeService) {
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookStoreUserRepository = bookStoreUserRepository;
        this.bookReviewLikeService = bookReviewLikeService;
    }

    @Test
    void saveReviewLikeUpVote() throws InterruptedException {
        BookStoreUser bookStoreUser = bookStoreUserRepository.findBookStoreUserByEmail("test1@gmail.com");
        BookReview bookReviewBefore = bookReviewRepository.findBookReviewById(1);
        Integer value = 1;
        List<BookReviewLike> bookReviewLikeListBefore = bookReviewLikeRepository.findBookReviewLikeByBookReview(bookReviewBefore);
        bookReviewBefore.setBookReviewLikes(bookReviewLikeListBefore);
        bookReviewLikeService.saveReviewLike(bookStoreUser, bookReviewBefore.getId(), value);
        Thread.sleep(500);
        List<BookReviewLike> bookReviewLikeListAfter = bookReviewLikeRepository.findBookReviewLikeByBookReview(bookReviewBefore);
        BookReview bookReviewAfter = bookReviewRepository.findBookReviewById(bookReviewBefore.getId());
        bookReviewAfter.setBookReviewLikes(bookReviewLikeListAfter);

        assertTrue(bookReviewLikeListBefore.size() < bookReviewLikeListAfter.size());
        assertTrue(bookReviewBefore.getLikeCount() < bookReviewAfter.getLikeCount());
        assertEquals(bookReviewBefore.getDisLikeCount(), bookReviewAfter.getDisLikeCount());
    }

    @Test
    void saveReviewLikeDownVote() throws InterruptedException {
        BookStoreUser bookStoreUser = bookStoreUserRepository.findBookStoreUserByEmail("test1@gmail.com");
        BookReview bookReviewBefore = bookReviewRepository.findBookReviewById(2);
        Integer value = -1;
        List<BookReviewLike> bookReviewLikeListBefore = bookReviewLikeRepository.findBookReviewLikeByBookReview(bookReviewBefore);
        bookReviewBefore.setBookReviewLikes(bookReviewLikeListBefore);
        bookReviewLikeService.saveReviewLike(bookStoreUser, bookReviewBefore.getId(), value);

        List<BookReviewLike> bookReviewLikeListAfter = bookReviewLikeRepository.findBookReviewLikeByBookReview(bookReviewBefore);
        BookReview bookReviewAfter = bookReviewRepository.findBookReviewById(bookReviewBefore.getId());
        bookReviewAfter.setBookReviewLikes(bookReviewLikeListAfter);

        assertTrue(bookReviewLikeListBefore.size() < bookReviewLikeListAfter.size());
        assertEquals(bookReviewBefore.getLikeCount(), bookReviewAfter.getLikeCount());
        assertTrue(bookReviewBefore.getDisLikeCount() < bookReviewAfter.getDisLikeCount());
    }
}