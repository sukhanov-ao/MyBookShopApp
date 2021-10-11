package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.book.rating.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<BookRating, Integer> {

    @Query(value = "SELECT * FROM book_rating where book_id = :bookId", nativeQuery = true)
    BookRating getRatingByBookId(Integer bookId);

//    @Query(value = "SELECT (five_stars * 5 + four_stars * 4 + three_stars * 3 + two_stars * 2 + one_star * 1) / (five_stars + four_stars + three_stars + two_stars + one_star) as avg FROM book_rating WHERE book_id = :bookId" , nativeQuery = true)
//    Integer getAverageRatingByBookId(Integer bookId);
//
//    @Query(value = "SELECT (five_stars + four_stars + three_stars + two_stars + one_star) FROM book_rating where book_id = :bookId", nativeQuery = true)
//    Integer getRatingVoteCountByBookId(Integer bookId);


    @Modifying
    @Query(value = "update book_rating set five_stars=:fiveStars, four_stars=:fourStars, one_star=:threeStars, three_stars=:twoStars, two_stars=:oneStar where id=:id", nativeQuery = true)
    void updateRating(Integer id, Integer oneStar, Integer twoStars, Integer threeStars, Integer fourStars, Integer fiveStars);
}
