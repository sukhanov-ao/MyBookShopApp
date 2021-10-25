package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.annotations.ApiExecutionTimeLoggable;
import com.example.mybookshopapp.data.ApiResponse;
import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.errors.BookStoreApiWrongParametersException;
import com.example.mybookshopapp.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(description = "book data api")
public class BooksRestApiController {

    private final BookService bookService;

    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @ApiExecutionTimeLoggable
    @GetMapping("/books/by-author")
    @ApiOperation("operation to get book list of bookshop by passed author first name")
    public ResponseEntity<List<Book>> booksByAuthor(@RequestParam("author") String authorName) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorName));
    }

    @ApiExecutionTimeLoggable
    @GetMapping("/books/by-title")
    @ApiOperation("get books by book title")
    public ResponseEntity<ApiResponse<Book>> booksByTitle(@RequestParam("title") String title) throws BookStoreApiWrongParametersException {
        ApiResponse<Book> response = new ApiResponse<>();
        List<Book> data = bookService.getBooksByTitle(title);
        response.setDebugMessage("successful request");
        response.setMessage("data size: " + data.size() + " elements");
        response.setStatus(HttpStatus.OK);
        response.setTimeStamp(LocalDateTime.now());
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    @ApiExecutionTimeLoggable
    @GetMapping("/books/by-price-range")
    @ApiOperation("get book price in range from min price to max price")
    public ResponseEntity<List<Book>> booksByPriceRange(@RequestParam("min") Integer min, @RequestParam("max") Integer max) {
        return ResponseEntity.ok(bookService.getBooksWithPriceBetween(min, max));
    }

    @ApiExecutionTimeLoggable
    @GetMapping("/books/with-max-discount")
    @ApiOperation("get list of books with max discount")
    public ResponseEntity<List<Book>> booksWithMaxDiscount() {
        return ResponseEntity.ok(bookService.getBooksWithMaxDiscount());
    }

    @ApiExecutionTimeLoggable
    @GetMapping("/books/bestsellers")
    @ApiOperation("get bestseller books (is_bestseller = 1)")
    public ResponseEntity<List<Book>> bestsellerBooks() {
        return ResponseEntity.ok(bookService.getBestsellers());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<Book>(HttpStatus.BAD_REQUEST, "Missing required parameters",
                exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookStoreApiWrongParametersException.class)
    public ResponseEntity<ApiResponse<Book>> handleBookStoreApiWrongParametersException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<Book>(HttpStatus.BAD_REQUEST, "Bad parameters value...",exception),
                HttpStatus.BAD_REQUEST);
    }
}
