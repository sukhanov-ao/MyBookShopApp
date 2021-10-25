package com.example.mybookshopapp.services;

import com.example.mybookshopapp.annotations.MethodExecutionTimeLoggable;
import com.example.mybookshopapp.data.book.rating.BookRating;
import com.example.mybookshopapp.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @MethodExecutionTimeLoggable
    public void save(BookRating bookRating) {
        ratingRepository.save(bookRating);
    }

    @MethodExecutionTimeLoggable
    public void updateRating(BookRating bookRating, Integer starValue) {
        switch (starValue) {
            case 1:
                bookRating.setOneStar(bookRating.getOneStar() + 1);
                break;
            case 2:
                bookRating.setTwoStars(bookRating.getTwoStars() + 1);
                break;
            case 3:
                bookRating.setThreeStars(bookRating.getThreeStars() + 1);
                break;
            case 4:
                bookRating.setFourStars(bookRating.getFourStars() + 1);
                break;
            case 5:
                bookRating.setFiveStars(bookRating.getFiveStars() + 1);
                break;
        }
        ratingRepository.save(bookRating);
    }
}