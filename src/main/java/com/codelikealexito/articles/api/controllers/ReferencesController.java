package com.codelikealexito.articles.api.controllers;

import com.codelikealexito.articles.api.dtos.CountReferencesResponseDto;
import com.codelikealexito.articles.api.dtos.ReferenceDto;
import com.codelikealexito.articles.api.services.ReferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/v1/api/article/reference")
public class ReferencesController {

    private final ReferenceService referenceService;

    public ReferencesController(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ReferenceDto>> getAllReferences() {
        return ResponseEntity.ok(referenceService.getAllReferences());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ReferenceDto>> getAllReferenceForArticle(@PathVariable(value = "id") Long articleId) {
        return ResponseEntity.ok(referenceService.getAllReferencesForArticle(articleId));
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Boolean>> postReferencesToDb() {
        return ResponseEntity.ok(referenceService.fillDatabaseEntityWithData());
    }

    @GetMapping("/count/{title}")
    public ResponseEntity<CountReferencesResponseDto> countedNumberOfCitedArticle(@PathVariable(value = "title") String articleTitle) {
        return ResponseEntity.ok(referenceService.countedNumberOfCitedArticle(articleTitle));
    }

}
