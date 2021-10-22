package com.example.mybookshopapp.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookStoreUserRepositoryTests {

    private BookStoreUserRepository bookStoreUserRepository;

    @Autowired
    public BookStoreUserRepositoryTests(BookStoreUserRepository bookStoreUserRepository) {
        this.bookStoreUserRepository = bookStoreUserRepository;
    }

    @Test
    void testAddNewUser() {
        BookStoreUser user = new BookStoreUser();
        user.setPassword("123467890");
        user.setPhone("9999999999");
        user.setName("Test");
        user.setEmail("test-user@mail.ru");

        assertNotNull(bookStoreUserRepository.save(user));
    }
}