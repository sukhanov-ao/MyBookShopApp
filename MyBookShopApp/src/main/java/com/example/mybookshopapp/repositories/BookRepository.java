package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.data.genre.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findBooksByAuthor_FirstName(String name);

    @Query("from Book")
    List<Book> customFindAllBooks();

    //NEW BOOK REST REPOSITORY COMMANDS

    List<Book> findBookByAuthorFirstNameContaining(String authorFirstName);

    List<Book> findBookByTitleContaining(String bookTitle);

    List<Book> findBooksByPriceOldBetween(Integer min, Integer max);

    List<Book> findBooksByPriceOldIs(Integer price);

    @Query("from Book where isBestseller = 1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage);

    Page<Book> findPageOfBooksByPubDateBetweenOrderByPubDateDesc(Date dateFrom, Date dateTo, Pageable nextPage);

    @Query(value = "SELECT * FROM books ORDER BY purchase_amount + 0.7 * in_cart_amount + 0.4 * postponed_amount DESC", nativeQuery = true)
    Page<Book> findPageOfBooksByPopularity(Pageable nextPage);

    @Query(value = "select b.* from books b join book2tag b2t on b.id = b2t.book_id where tag_id = :tagId", nativeQuery = true)
    Page<Book> findBooksByTag(Integer tagId, Pageable nextPage);

    Page<Book> findByAuthorId(Integer authorId, Pageable nextPage);

    Page<Book> findAllByGenre(Genre genre, Pageable nextPage);

    Page<Book> getPageBookByGenreId(Integer genreId, Pageable nextPage);

    Book findBookBySlug(String slug);

    List<Book> findBooksBySlugIn(String[] slugs);
}
