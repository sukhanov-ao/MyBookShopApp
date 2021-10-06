package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

}
