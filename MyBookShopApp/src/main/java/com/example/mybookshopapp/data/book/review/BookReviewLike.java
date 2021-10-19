package com.example.mybookshopapp.data.book.review;

import com.example.mybookshopapp.security.BookStoreUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "book_review_like")
public class BookReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    private BookReview bookReview;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private BookStoreUser user;

    private LocalDateTime time;
    private Integer value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BookReview getBookReview() {
        return bookReview;
    }

    public void setBookReview(BookReview bookReview) {
        this.bookReview = bookReview;
    }

    public BookStoreUser getUser() {
        return user;
    }

    public void setUser(BookStoreUser user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
