package com.codelikealexito.articles.api.services;

import com.codelikealexito.articles.api.dtos.ArticleRequestDto;
import com.codelikealexito.articles.api.dtos.ArticleResponseDTO;
import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.exceptions.CustomResponseStatusException;
import com.codelikealexito.articles.api.repositories.ArticleRepository;
import com.codelikealexito.articles.api.util.ArticleResponseDTOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleResponseDTOMapper articleResponseDTOMapper;

    @InjectMocks
    private ArticleService articleService;

    @Test
    public void getAllArticles_returnsListOfArticles() {
        // Given
        List<Article> articles = Arrays.asList(Mocks.mockArticle(), Mocks.mockArticle());
        when(articleRepository.findAll()).thenReturn(articles);
        when(articleResponseDTOMapper.apply(any())).thenReturn(Mocks.createMockArticleResponseDTO());

        // When
        ResponseEntity<List<ArticleResponseDTO>> response = articleService.getAllArticles();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(articles.size(), response.getBody().size());
    }

    @Test
    public void getArticleByTitle_returnsMatchingArticles() {
        // Given
        List<Article> articles = Arrays.asList(
                Mocks.createMockArticleChatGpt()
        );
        when(articleRepository.findAll()).thenReturn(articles);
        when(articleResponseDTOMapper.apply(any())).thenReturn(Mocks.createMockArticleResponseDTO());

        // When
        List<ArticleResponseDTO> response = articleService.getArticleByTitle("Mock Article Title");

        // Then
        assertEquals(1, response.size());
        assertEquals("Mock Article Title", response.get(0).title());
        assertEquals("Mock abstract description.", response.get(0).abstractDescription());
    }

    @Test
    public void getArticleById_returnsMatchingArticle() throws Exception {
        // Given
        Long articleId = null;
        Article article = Mocks.mockArticle();
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(articleResponseDTOMapper.apply(article)).thenReturn(Mocks.createMockArticleResponseDTO());

        // When
        ArticleResponseDTO response = articleService.getArticleById(articleId);

        // Then
        assertNotNull(response);
        assertEquals(articleId, response.articleId());
        assertEquals("Mock Article Title", response.title());
        assertEquals("Mock abstract description.", response.abstractDescription());
    }

    @Test(expected = CustomResponseStatusException.class)
    public void getArticleById_articleNotFound_throwsCustomResponseStatusException() throws Exception {
        // Given
        Long articleId = 1L;
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // When
        articleService.getArticleById(articleId);

        // Then expect CustomResponseStatusException to be thrown
    }

    @Test
    public void addArticle_returnsNewArticle() {
        // Given
        ArticleRequestDto articleRequestDto = Mocks.createMockArticleRequestDto();
        Article article = Mocks.mockArticle();
        when(articleRepository.save(any())).thenReturn(article);

        // When
        Article response = articleService.addArticle(articleRequestDto);

        // Then
        assertNotNull(response);
        assertEquals("Title", response.getTitle());
        assertEquals("mockAbstractDescription", response.getAbstractDescription());
    }

    @Test
    public void editArticle_returnsUpdatedArticle() throws Exception {
        // Given
        Long articleId = null;
        ArticleRequestDto articleRequestDto = Mocks.createMockArticleRequestDto();
        Article currentArticle = Mocks.mockArticle();
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(currentArticle));
        when(articleRepository.save(any())).thenReturn(currentArticle);

        // When
        Article response = articleService.editArticle(articleId, articleRequestDto);

        // Then
        assertNotNull(response);
        assertEquals(articleId, response.getArticleId());
        assertEquals("Title", response.getTitle());
    }
}