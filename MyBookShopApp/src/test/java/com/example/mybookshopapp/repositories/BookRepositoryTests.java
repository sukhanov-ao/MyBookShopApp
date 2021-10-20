package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.book.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookRepositoryTests {

    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTests(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void findBooksByAuthor_FirstName() {
        String token = "Edyth";
        List<Book> bookListByAuthorsFirstName = bookRepository.findBooksByAuthor_FirstName(token);

        assertNotNull(bookListByAuthorsFirstName);
        assertFalse(bookListByAuthorsFirstName.isEmpty());
        for (Book book : bookListByAuthorsFirstName) {
            Logger.getLogger(this.getClass().getSimpleName()).info(book.toString());
            assertThat(book.getAuthor().getFirstName().contains(token));
        }
    }

    @Test
    void findBookByTitleContaining() {
        String token = "fish";
        List<Book> bookListByTitleContaining = bookRepository.findBookByTitleContaining(token);
        assertNotNull(bookListByTitleContaining);
        assertFalse(bookListByTitleContaining.isEmpty());
        for (Book book : bookListByTitleContaining) {
            Logger.getLogger(this.getClass().getSimpleName()).info(book.toString());
            assertThat(book.getTitle().contains(token));
        }
    }

    @Test
    void getBestsellers() {
        List<Book> bestSellersBooks = bookRepository.getBestsellers();
        assertNotNull(bestSellersBooks);
        assertFalse(bestSellersBooks.isEmpty());
        assertThat(bestSellersBooks.size()).isGreaterThan(1);
    }

    @Test
    void getMaxDiscount() {
        List<Book> maxDiscountBooks = bookRepository.getBooksWithMaxDiscount();
        assertNotNull(maxDiscountBooks);
        assertFalse(maxDiscountBooks.isEmpty());
        assertThat(maxDiscountBooks.size()).isGreaterThan(1);
    }
}