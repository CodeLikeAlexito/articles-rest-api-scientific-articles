package com.codelikealexito.articles.api.controllers;

import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.services.ArticleService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/article")
public class ArticleContoller {

    private final ArticleService bookService;

    public ArticleContoller(ArticleService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public List<Article> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{title}")
    public Article getBookByTitle(@PathVariable(value = "title") String title) {
        Article searchedBook = bookService.getBookByTitle(title);
        return searchedBook;
    }
}
