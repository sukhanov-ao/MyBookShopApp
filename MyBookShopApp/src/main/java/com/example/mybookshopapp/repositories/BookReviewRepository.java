package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.book.review.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewRepository extends JpaRepository<BookReview, Integer> {

    BookReview findBookReviewById(Integer id);
}
