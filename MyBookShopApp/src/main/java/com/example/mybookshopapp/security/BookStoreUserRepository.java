package com.example.mybookshopapp.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStoreUserRepository extends JpaRepository<BookStoreUser, Integer> {

    BookStoreUser findBookStoreUserByEmail(String email);
}
