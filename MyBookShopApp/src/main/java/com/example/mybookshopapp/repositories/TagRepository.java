package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}
