package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.data.BooksPageDTO;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TagsPageController {

    private final TagService tagService;
    private final BookService bookService;

    @Autowired
    public TagsPageController(TagService tagService, BookService bookService) {
        this.tagService = tagService;
        this.bookService = bookService;
    }

    @GetMapping("/tags/")
    public String getAllTags(Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        return "tags/index";
    }
    @GetMapping("/tags/{tagId:\\d+}")
    public String getTag(@PathVariable Integer tagId, Model model) {
        model.addAttribute("tagBooks", bookService.getBooksByTag(tagId, 0, 20));
        model.addAttribute("tag", tagService.getTag(tagId));
        return "tags/index";
    }

    @GetMapping("/books/tag/page/{tagId:\\d+}")
    @ResponseBody
    public BooksPageDTO getNextPage(@RequestParam("offset") Integer offset,
                                    @RequestParam("limit") Integer limit,
                                    @PathVariable(value = "tagId", required = false) Integer tagId) {
        return new BooksPageDTO(bookService.getBooksByTag(tagId, offset, limit).getContent());
    }
}
