package com.example.mybookshopapp.services;

import com.example.mybookshopapp.annotations.MethodExecutionTimeLoggable;
import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.data.genre.Genre;
import com.example.mybookshopapp.errors.BookStoreApiWrongParametersException;
import com.example.mybookshopapp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @MethodExecutionTimeLoggable
    public List<Book> getBooksData() {
        return bookRepository.findAll();
    }

    //NEW BOOK SERVICE METHODS

    @MethodExecutionTimeLoggable
    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBookByAuthorFirstNameContaining(authorName);
    }

    @MethodExecutionTimeLoggable
    public List<Book> getBooksByTitle(String title) throws BookStoreApiWrongParametersException {
        if (title.equals("") || title.length() <= 1) {
            throw new BookStoreApiWrongParametersException("Wrong values passed to one or more parameters");
        } else {
            List<Book> data = bookRepository.findBookByTitleContaining(title);
            if(!data.isEmpty()) {
                return data;
            } else {
                throw new BookStoreApiWrongParametersException("No data found with specified parameters");
            }
        }
    }

    @MethodExecutionTimeLoggable
    public List<Book> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBooksByPriceOldBetween(min, max);
    }

    @MethodExecutionTimeLoggable
    public List<Book> getBooksWithPrice(Integer price) {
        return bookRepository.findBooksByPriceOldIs(price);
    }

    @MethodExecutionTimeLoggable
    public List<Book> getBooksWithMaxDiscount() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    @MethodExecutionTimeLoggable
    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    @MethodExecutionTimeLoggable
    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    @MethodExecutionTimeLoggable
    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContaining(searchWord, nextPage);
    }

    @MethodExecutionTimeLoggable
    public Page<Book> getMostRecentBooks(Date dateFrom, Date dateTo, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findPageOfBooksByPubDateBetweenOrderByPubDateDesc(dateFrom, dateTo, nextPage);
    }

    @MethodExecutionTimeLoggable
    public Page<Book> getMostPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findPageOfBooksByPopularity(nextPage);
    }

    @MethodExecutionTimeLoggable
    public Page<Book> getBooksByTag(Integer tagId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTag(tagId, nextPage);
    }

    @MethodExecutionTimeLoggable
    public Page<Book> getBooksByAuthorId(Integer authorId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findByAuthorId(authorId, nextPage);
    }

    @MethodExecutionTimeLoggable
    public Page<Book> getPageBookByGenre(Genre genre, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAllByGenre(genre, nextPage);
    }

    @MethodExecutionTimeLoggable
    public Page<Book> getPageBookByGenreId(Integer genreId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getPageBookByGenreId(genreId, nextPage);
    }
}
