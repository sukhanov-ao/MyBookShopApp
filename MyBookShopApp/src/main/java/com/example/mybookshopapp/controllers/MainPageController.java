package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BooksPageDTO;
import com.example.mybookshopapp.data.SearchWordDTO;
import com.example.mybookshopapp.data.book.Book;
import com.example.mybookshopapp.data.tag.Tag;
import com.example.mybookshopapp.errors.EmptySearchException;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class MainPageController {

    private final BookService bookService;
    private final TagService tagService;
    private final Calendar calendar = Calendar.getInstance();

    @Autowired
    public MainPageController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }

    @ModelAttribute("books")
    public List<Book> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }

    @ModelAttribute("searchWordDTO")
    public SearchWordDTO searchWordDTO() {
        return new SearchWordDTO();
    }

    @ModelAttribute("searchResults")
    public List<Book> searchResults() {
        return new ArrayList<>();
    }

    @ModelAttribute("tags")
    public List<Tag> tags() {
        return tagService.getTagData();
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDTO getBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDTO(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDTO searchWordDTO,
                                   Model model) throws EmptySearchException {
        if(searchWordDTO!= null) {
            model.addAttribute("searchWordDTO", searchWordDTO);
            model.addAttribute("searchResults",
                    bookService.getPageOfSearchResultBooks(searchWordDTO.getExample(), 0, 20).getContent());
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по null невозможен");
        }
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDTO getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDTO searchWordDTO) {
        return new BooksPageDTO(bookService.getPageOfSearchResultBooks(searchWordDTO.getExample(), offset, limit).getContent());
    }
}
