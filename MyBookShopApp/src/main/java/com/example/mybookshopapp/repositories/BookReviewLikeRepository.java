package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.book.review.BookReview;
import com.example.mybookshopapp.data.book.review.BookReviewLike;
import com.example.mybookshopapp.security.BookStoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Integer> {
    BookReviewLike findBookReviewLikeByUserAndBookReview(BookStoreUser user, BookReview bookReview);
}
