package com.example.mybookshopapp.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStoreUserRepository extends JpaRepository<BookStoreUser, Integer> {

    BookStoreUser findBookStoreUserByEmail(String email);

    BookStoreUser findBookStoreUserByPhone(String phone);

    BookStoreUser findBookStoreUserById(Integer id);
}
