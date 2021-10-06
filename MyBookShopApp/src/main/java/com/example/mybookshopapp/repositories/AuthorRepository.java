package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.book.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Author getById(Integer id);

//    @Query(value = "SELECT * FROM authors ORDER BY last_name", nativeQuery = true)
    List<Author> findAllByOrderByLastNameAsc();
}
