package com.example.mybookshopapp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class CartPageControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    CartPageControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void cartPageAccessTest() throws Exception {
        mockMvc.perform(get("/books/cart"))
                .andDo(print())
                .andExpect(content().string(containsString("")))
                .andExpect(status().isOk());
    }

    @Test
    void handleChangeBookStatus() throws Exception {
        mockMvc.perform(post("/books/changeBookStatus/book-iod-942"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(cookie().value("cartContents", "book-iod-942"));
    }

    @Test
    void handleChangeBookStatusDeleteFromCart() throws Exception {
        mockMvc.perform(post("/books/changeBookStatus/cart/remove/book-iod-942")
                        .cookie(new Cookie("cartContents", "book-iod-942")))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(cookie().value("cartContents", ""));
    }
}