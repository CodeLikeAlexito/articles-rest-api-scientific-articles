package com.codelikealexito.articles.api.services;

import com.codelikealexito.articles.api.dtos.ArticleRequestDto;
import com.codelikealexito.articles.api.dtos.ArticleResponseDTO;
import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.enums.Status;
import com.codelikealexito.articles.api.exceptions.CustomResponseStatusException;
import com.codelikealexito.articles.api.repositories.ArticleRepository;
import com.codelikealexito.articles.api.util.ArticleResponseDTOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import static com.codelikealexito.articles.api.util.Helper.*;

@Service
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleResponseDTOMapper articleResponseDTOMapper;

    public ArticleService(ArticleRepository articleRepository, ArticleResponseDTOMapper articleResponseDTOMapper) {
        this.articleRepository = articleRepository;
        this.articleResponseDTOMapper = articleResponseDTOMapper;
    }

    public ResponseEntity<List<ArticleResponseDTO>> getAllArticles() {
        List<ArticleResponseDTO> articleResponseDTOList = getAllArticlesFromDb()
                .stream()
                .map(articleResponseDTOMapper)
                .collect(Collectors.toList());
        return ResponseEntity.ok(articleResponseDTOList);
    }

    public List<ArticleResponseDTO> getArticleByTitle(String title) {
        return articleRepository.findAll()
                .stream()
                .filter(article -> article.getTitle().toLowerCase(Locale.ROOT).contains(title.toLowerCase(Locale.ROOT)))
                .map(articleResponseDTOMapper)
                .collect(Collectors.toList());
    }

    public ArticleResponseDTO getArticleById(Long id) throws Exception {
        Optional<Article> article = articleRepository.findById(id);
        if(article.isEmpty()){
            throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, "SOME_ERR_CODE", String.format("Article with id %d is not found", id));
        }

        return article.map(articleResponseDTOMapper).get();
    }


    public Article addArticle(ArticleRequestDto articleRequestDto) {

        if(articleRequestDto.getArticlePdf() == null) {
            byte[] articleAsByteArray = saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/pdf/an-efficient-multi-objective-meta-heuristic-method-for-probabilistic-transmission-network-planning.pdf");
            String articleAsBase64 = convertFromByteArrayToBase64(articleAsByteArray);
            articleRequestDto.setArticlePdf(articleAsBase64);
        }

        if(articleRequestDto.getCoverPageImage() == null) {
            byte[] articleCoverAsByteArray = saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/images/Book1.jpg");
            String articleCoverAsBase64 = convertFromByteArrayToBase64(articleCoverAsByteArray);
            articleRequestDto.setCoverPageImage(articleCoverAsBase64);
        }

        byte[] articleCoverAsByteArray = convertFromBase64StringToByteArray(articleRequestDto.getCoverPageImage());
        byte[] articlePdfAsByteArray = convertFromBase64StringToByteArray(articleRequestDto.getArticlePdf());

        String[] authors = articleRequestDto.getAuthors().trim().split(",");
        String[] keywords = articleRequestDto.getKeywords().trim().split(",");

        Article article = Article.createArticle(articleRequestDto.getArticleId(), articleRequestDto.getTitle(), articleRequestDto.getYearPublished(), authors
                , keywords, articleCoverAsByteArray, articlePdfAsByteArray, articleRequestDto.getAbstractDescription()
                , articleRequestDto.getAcademicJournal(), articleRequestDto.getFieldOfScience(), Status.PENDING, articleRequestDto.getCreator());
        return articleRepository.save(article);
    }

    public Article editArticle(Long articleId, ArticleRequestDto articleRequestDto) throws Exception {
        Article currentArticle = articleRepository.findById(articleId)
                .orElseThrow(() ->  new Exception("Article does not exists. Article id: " + articleRequestDto.getArticleId()));

        if(articleRequestDto.getArticlePdf() == null) {
            byte[] articleAsByteArray = saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/pdf/an-efficient-multi-objective-meta-heuristic-method-for-probabilistic-transmission-network-planning.pdf");
            String articleAsBase64 = convertFromByteArrayToBase64(articleAsByteArray);
            articleRequestDto.setArticlePdf(articleAsBase64);
        }

        if(articleRequestDto.getCoverPageImage() == null) {
            byte[] articleCoverAsByteArray = saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/images/Book1.jpg");
            String articleCoverAsBase64 = convertFromByteArrayToBase64(articleCoverAsByteArray);
            articleRequestDto.setCoverPageImage(articleCoverAsBase64);
        }

        byte[] articlePdfAsByteArray = convertFromBase64StringToByteArray(articleRequestDto.getArticlePdf());
        byte[] articleCoverAsByteArray = convertFromBase64StringToByteArray(articleRequestDto.getCoverPageImage());

        Status status = Status.valueOf(articleRequestDto.getStatus());

        String[] authors = articleRequestDto.getAuthors().trim().split(",");
        String[] keywords = articleRequestDto.getKeywords().trim().split(",");

        Article article = Article.updateArticle(currentArticle.getArticleId(), articleRequestDto.getTitle(), articleRequestDto.getYearPublished(), authors
                , keywords, articleCoverAsByteArray, articlePdfAsByteArray, articleRequestDto.getAbstractDescription()
                , articleRequestDto.getAcademicJournal(), articleRequestDto.getFieldOfScience(), status , articleRequestDto.getCreator());
        return articleRepository.save(article);
    }

    public Map<String, Boolean> deleteArticle(Long articleId) throws Exception {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() ->  new Exception("Article does not exists. Article id: " + articleId));

        articleRepository.delete(article);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }

    private static String convertFromStringArrayToString(String[] strArr) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < strArr.length; i++) {
            sb.append(strArr[i]);
        }
        String resStr = Arrays.toString(strArr);
        String newStr = resStr.replace("[", "");
        String newestStr = newStr.replace("]", "");
        return newestStr;
    }

    private List<Article> getAllArticlesFromDb() {
        return articleRepository.findAll();
    }

    public List<ArticleResponseDTO> getAllArticlesByArticleStatus(String status) {
        List<Article> articlesWithStatus = articleRepository.findAll()
                .stream()
                .filter(article -> status.equalsIgnoreCase(article.getStatus()))
                .toList();

        if(articlesWithStatus.size() == 0){
            throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, "SOME_ERROR_CODE", String.format("There are no articles with status: %s", status));
        }

        return articlesWithStatus
                .stream()
                .map(articleResponseDTOMapper)
                .collect(Collectors.toList());
    }

    public List<ArticleResponseDTO> getArticleByKeywords(String keyword) {

        List<Article> articles = articleRepository.findAll();

        return articles
                .stream()
                .filter(article -> Arrays.stream(article.getKeywords()).anyMatch(keyword::contains))
                .map(articleResponseDTOMapper)
                .collect(Collectors.toList());
    }

    public List<ArticleResponseDTO> getArticleByFieldOfScience(String science) {
        return articleRepository.findAll()
                .stream()
                .filter(article -> article.getFieldOfScience().toLowerCase().contains(science.toLowerCase()))
                .map(articleResponseDTOMapper)
                .collect(Collectors.toList());
    }

    public List<ArticleResponseDTO> getArticlesForUser(String username) {
        return articleRepository.findAll()
                .stream()
                .filter(article -> article.getCreator().toLowerCase().contains(username.toLowerCase()))
                .map(articleResponseDTOMapper)
                .collect(Collectors.toList());
    }
}
