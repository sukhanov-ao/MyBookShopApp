package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.book.review.BookReview;
import com.example.mybookshopapp.data.book.review.BookReviewLike;
import com.example.mybookshopapp.repositories.BookReviewLikeRepository;
import com.example.mybookshopapp.repositories.BookReviewRepository;
import com.example.mybookshopapp.security.BookStoreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class BookReviewLikeService {

    private final BookReviewLikeRepository bookReviewLikeRepo;
    private final BookReviewRepository reviewRepository;

    @Autowired
    public BookReviewLikeService(BookReviewLikeRepository bookReviewLikeRepository, BookReviewRepository reviewRepository) {
        this.bookReviewLikeRepo = bookReviewLikeRepository;
        this.reviewRepository = reviewRepository;
    }

    private BookReviewLike getBookReviewLikeByUserAndReview(BookStoreUser user, BookReview bookReview) {
        return bookReviewLikeRepo.findBookReviewLikeByUserAndBookReview(user, bookReview);
    }

    public Boolean saveReviewLike(BookStoreUser user, Integer bookReviewId, Integer value) {
        BookReview bookReview = reviewRepository.findBookReviewById(bookReviewId);
        BookReviewLike reviewLike = getBookReviewLikeByUserAndReview(user, bookReview);
        if (reviewLike == null) {
            reviewLike = new BookReviewLike();
            reviewLike.setUser(user);
            reviewLike.setBookReview(bookReview);
            reviewLike.setTime(LocalDateTime.now());
            reviewLike.setValue(value);
            bookReviewLikeRepo.save(reviewLike);
            return true;
        }
        reviewLike.setValue(value);
        reviewLike.setTime(LocalDateTime.now());
        bookReviewLikeRepo.save(reviewLike);
        return true;
    }
}
