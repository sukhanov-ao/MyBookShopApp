package com.example.mybookshopapp.data.book.rating;

import com.example.mybookshopapp.data.book.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Objects;

@Entity
@Table(name = "book_rating")
@Transactional
public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @JsonIgnore
    private Book book;

    @Column(name = "one_star")
    private Integer oneStar;

    @Column(name = "two_stars")
    private Integer twoStars;

    @Column(name = "three_stars")
    private Integer threeStars;

    @Column(name = "four_stars")
    private Integer fourStars;

    @Column(name = "five_stars")
    private Integer fiveStars;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getOneStar() {
        return oneStar;
    }

    public void setOneStar(Integer oneStar) {
        this.oneStar = oneStar;
    }

    public Integer getTwoStars() {
        return twoStars;
    }

    public void setTwoStars(Integer twoStars) {
        this.twoStars = twoStars;
    }

    public Integer getThreeStars() {
        return threeStars;
    }

    public void setThreeStars(Integer threeStars) {
        this.threeStars = threeStars;
    }

    public Integer getFourStars() {
        return fourStars;
    }

    public void setFourStars(Integer fourStars) {
        this.fourStars = fourStars;
    }

    public Integer getFiveStars() {
        return fiveStars;
    }

    public void setFiveStars(Integer fiveStars) {
        this.fiveStars = fiveStars;
    }

    public void setStarByValue(Integer starValue) {
        switch (starValue) {
            case 1:
                this.setOneStar(getOneStar() + 1);
                break;
            case 2:
                this.setTwoStars(getTwoStars() + 1);
                break;
            case 3:
                this.setThreeStars(getThreeStars() + 1);
                break;
            case 4:
                this.setFourStars(getFourStars() + 1);
                break;
            case 5:
                this.setFiveStars(getFiveStars() + 1);
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookRating that = (BookRating) o;
        return Objects.equals(id, that.id) && Objects.equals(book, that.book) && Objects.equals(oneStar, that.oneStar) && Objects.equals(twoStars, that.twoStars) && Objects.equals(threeStars, that.threeStars) && Objects.equals(fourStars, that.fourStars) && Objects.equals(fiveStars, that.fiveStars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, oneStar, twoStars, threeStars, fourStars, fiveStars);
    }

    @Override
    public String toString() {
        return "BookRating{" +
                "id=" + id +
                ", book=" + book +
                ", oneStar=" + oneStar +
                ", twoStars=" + twoStars +
                ", threeStars=" + threeStars +
                ", fourStars=" + fourStars +
                ", fiveStars=" + fiveStars +
                '}';
    }

    public Integer getAverageRating() {
        if (this.oneStar != 0 && this.twoStars != 0 && this.threeStars != 0 && this.fourStars != 0 && this.fiveStars != 0) {
            return Math.toIntExact((this.oneStar + this.twoStars * 2 + this.threeStars * 3 + this.fourStars * 4 + this.fiveStars * 5)
                    / (this.oneStar + this.twoStars + this.threeStars + this.fourStars + this.fiveStars));
        } else {
            return 0;
        }
    }

    public Integer getAllVoteAmount() {
        return this.oneStar + this.twoStars + this.threeStars + this.fourStars + this.fiveStars;
    }
}
