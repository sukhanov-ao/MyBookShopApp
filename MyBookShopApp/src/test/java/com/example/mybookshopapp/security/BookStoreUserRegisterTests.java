package com.example.mybookshopapp.security;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookStoreUserRegisterTests {

    private final BookStoreUserRegister bookStoreUserRegister;
    private final PasswordEncoder passwordEncoder;
    private RegistrationForm registrationForm;

    @MockBean
    private BookStoreUserRepository bookStoreUserRepositoryMock;

    @Autowired
    public BookStoreUserRegisterTests(BookStoreUserRegister bookStoreUserRegister, PasswordEncoder passwordEncoder) {
        this.bookStoreUserRegister = bookStoreUserRegister;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.org");
        registrationForm.setName("Tester");
        registrationForm.setPassword("password");
        registrationForm.setPhone("89999999999");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
    }

    @Test
    void registerNewUser() {
        BookStoreUser user = bookStoreUserRegister.registerNewUser(registrationForm);
        assertNotNull(user);
        assertTrue(passwordEncoder.matches(registrationForm.getPassword(), user.getPassword()));
        assertTrue(CoreMatchers.is(user.getPhone()).matches(registrationForm.getPhone()));
        assertTrue(CoreMatchers.is(user.getName()).matches(registrationForm.getName()));
        assertTrue(CoreMatchers.is(user.getEmail()).matches(registrationForm.getEmail()));

        Mockito.verify(bookStoreUserRepositoryMock, Mockito.times(1))
                .save(Mockito.any(BookStoreUser.class));
    }

    @Test
    void registerNewUserFail() {
        Mockito.doReturn(new BookStoreUser())
                .when(bookStoreUserRepositoryMock)
                .findBookStoreUserByEmail(registrationForm.getEmail());

        BookStoreUser user = bookStoreUserRegister.registerNewUser(registrationForm);
        assertNull(user);
    }
}