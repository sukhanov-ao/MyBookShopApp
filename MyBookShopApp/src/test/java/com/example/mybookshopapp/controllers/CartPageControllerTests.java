package com.example.mybookshopapp.controllers;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void handleRemoveBookFromCartRequest() {
    }

    @Test
    void handleChangeBookStatus() throws Exception {
        mockMvc.perform(post("/books/changeBookStatus/book-iod-942"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void handleChangeBookStatusDeleteFromCart() throws Exception {
        mockMvc.perform(post("/books/changeBookStatus/cart/remove/book-iod-942"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}