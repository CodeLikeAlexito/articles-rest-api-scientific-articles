package com.codelikealexito.articles.api.controllers;

import com.codelikealexito.articles.api.dtos.ArticleResponseDto;
import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.services.ArticleService;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/article")
public class ArticleContoller {

    private final ArticleService bookService;

    public ArticleContoller(ArticleService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ArticleResponseDto>> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{title}")
    public ResponseEntity<List<ArticleResponseDto>> getBookByTitle(@PathVariable(value = "title") String title) {
        List<ArticleResponseDto> searchedBook = bookService.getBookByTitle(title);
        return ResponseEntity.ok(searchedBook);
    }
}
