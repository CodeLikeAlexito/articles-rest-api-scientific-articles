package com.codelikealexito.articles.api.controllers;

import com.codelikealexito.articles.api.dtos.ArticleRequestDto;
import com.codelikealexito.articles.api.dtos.ArticleResponseDto;
import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.services.ArticleService;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/v1/api/article")
public class ArticleContoller {

    private final ArticleService articleService;

    public ArticleContoller(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ArticleResponseDto>> getAllArticles() {
        return articleService.getAllBooks();
    }

    @GetMapping("/{title}")
    public ResponseEntity<List<ArticleResponseDto>> getArticleByTitle(@PathVariable(value = "title") String title) {
        List<ArticleResponseDto> searchedArticle = articleService.getArticleByTitle(title);
        return ResponseEntity.ok(searchedArticle);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ArticleResponseDto> getArticleById(@PathVariable(value = "id") Long id) throws Exception {
        ArticleResponseDto getArticle = articleService.getArticleById(id);
        return ResponseEntity.ok(getArticle);
    }

    @PostMapping("/")
    public ResponseEntity<Article> addArticle(@RequestBody ArticleRequestDto articleRequestDto) {
        return ResponseEntity.ok(articleService.addArticle(articleRequestDto));
    }
}
