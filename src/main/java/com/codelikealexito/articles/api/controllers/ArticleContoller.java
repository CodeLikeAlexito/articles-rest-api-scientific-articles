package com.codelikealexito.articles.api.controllers;

import com.codelikealexito.articles.api.dtos.ArticleRequestDto;
import com.codelikealexito.articles.api.dtos.ArticleResponseDto;
import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.services.ArticleService;
import java.util.List;
import java.util.Map;

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
        return articleService.getAllArticles();
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

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ArticleResponseDto>> getAllArticleByArticleStatus(@PathVariable(value = "status") String status) {
        List<ArticleResponseDto> searchedArticles = articleService.getAllArticlesByArticleStatus(status);

        return ResponseEntity.ok(searchedArticles);
    }

    @PostMapping("/")
    public ResponseEntity<Article> addArticle(@RequestBody ArticleRequestDto articleRequestDto) {
        return ResponseEntity.ok(articleService.addArticle(articleRequestDto));
    }

    @PutMapping("/")
    public ResponseEntity<Article> editArticle(@RequestBody ArticleRequestDto articleRequestDto) throws Exception {
        return ResponseEntity.ok(articleService.editArticle(articleRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteArticle(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(articleService.deleteArticle(id));
    }
}
