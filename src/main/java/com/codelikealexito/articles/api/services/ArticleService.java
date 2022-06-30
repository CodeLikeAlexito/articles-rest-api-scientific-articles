package com.codelikealexito.articles.api.services;

import com.codelikealexito.articles.api.dtos.ArticleRequestDto;
import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.dtos.ArticleResponseDto;
import com.codelikealexito.articles.api.enums.Status;
import com.codelikealexito.articles.api.exceptions.CustomResponseStatusException;
import com.codelikealexito.articles.api.repositories.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ResponseEntity<List<ArticleResponseDto>> getAllArticles() {
        return ResponseEntity.ok(getAllArticlesResponseDto());
    }

    public List<ArticleResponseDto> getArticleByTitle(String title) {
        //TODO change from DB not from mocked method
//        List<Article> articles =  articleRepository.findByTitle(title);
        List<Article> articles =  articleRepository.findAll()
                .stream()
                .filter(article -> article.getTitle().toLowerCase(Locale.ROOT).contains(title.toLowerCase(Locale.ROOT)))
                .toList();

        //validateSearchString

        List<ArticleResponseDto> responseList = new ArrayList<>();
        IntStream.range(0, articles.size())
                .forEach( index -> {
                    responseList.add(setArticleResponseDto(Optional.ofNullable(articles.get(index))));
                }
            );

        if(responseList.size() == 0)
            throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, "SOME_ERR_CODE", String.format("Article %s is not found", title));

        return responseList;
    }

    public ArticleResponseDto getArticleById(Long id) throws Exception {
        Optional<Article> article = articleRepository.findById(id);
        if(article.isEmpty()){
            throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, "SOME_ERR_CODE", String.format("Article with id %d is not found", id));
        }

        return setArticleResponseDto(article);
    }


    public Article addArticle(ArticleRequestDto articleRequestDto) {
        //TODO If article pdf and cover page are not passed in the request body, i hardcode them
        // TODO Article cover image can stay like this. If image is not selected i should pass default cover image
        // TODO Article pdf should never be null, so later I need to delete this if
        if(articleRequestDto.getArticlePdf() == null) {
            byte[] articleAsByteArray = saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/pdf/an-efficient-multi-objective-meta-heuristic-method-for-probabilistic-transmission-network-planning.pdf");
            String articleAsBase64 = convertFromByteArrayToBase64(articleAsByteArray);
            articleRequestDto.setArticlePdf(articleAsBase64);
//            articleRequestDto.setArticlePdf(articleAsByteArray);
        }

        if(articleRequestDto.getCoverPageImage() == null) {
            byte[] articleCoverAsByteArray = saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/images/Book1.jpg");
            String articleCoverAsBase64 = convertFromByteArrayToBase64(articleCoverAsByteArray);
            articleRequestDto.setCoverPageImage(articleCoverAsBase64);
//            articleRequestDto.setCoverPageImage(articleCoverAsByteArray);
        }

        byte[] articleCoverAsByteArray = convertFromBase64StringToByteArray(articleRequestDto.getCoverPageImage());
//        System.out.println(articleCoverAsByteArray);
//        byte[] articleCoverAsByteArray = articleRequestDto.getCoverPageImage();
        byte[] articlePdfAsByteArray = convertFromBase64StringToByteArray(articleRequestDto.getArticlePdf());
//        byte[] articlePdfAsByteArray = articleRequestDto.getArticlePdf();

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

        byte[] articlePdfAsByteArray = convertFromBase64StringToByteArray(articleRequestDto.getArticlePdf());
//        byte[] articlePdfAsByteArray = convertFromBase64StringToByteArray(currentArticle.getArticlePdf());
//        byte[] articleCoverAsByteArray = articleRequestDto.getCoverPageImage();
        byte[] articleCoverAsByteArray = convertFromBase64StringToByteArray(articleRequestDto.getCoverPageImage());
//        byte[] articlePdfAsByteArray = articleRequestDto.getArticlePdf();

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

    private ArticleResponseDto setArticleResponseDto(Optional<Article> article) {
        return ArticleResponseDto.builder()
                .articleId(article.get().getArticleId())
                .title(article.get().getTitle())
                .yearPublished(article.get().getYearPublished())
                .authors(convertFromStringArrayToString(article.get().getAuthors()))
                .keywords(convertFromStringArrayToString(article.get().getKeywords()))
                .coverPage(convertFromByteArrayToBase64(article.get().getCoverPageImage()))
//                .coverPage(article.get().getCoverPageImage())
                .articlePdf(convertFromByteArrayToBase64(article.get().getArticlePdf()))
//                .articlePdf(article.get().getArticlePdf())
                .abstractDescription(article.get().getAbstractDescription())
                .academicJournal(article.get().getAcademicJournal())
                .fieldOfScience(article.get().getFieldOfScience())
                .creator(article.get().getCreator())
                .status(article.get().getStatus())
                .build();
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

    //TODO Transforming from Article Entity to ArticleResponseDto, needs to be used only if there are properties in the DTO, that are not in the entity
    private List<ArticleResponseDto> getAllArticlesResponseDto() {
        List<Article> articlesFromStorage = getAllArticlesFromDb();
        List<ArticleResponseDto> articlesResponse = new ArrayList<>();

        IntStream.range(0, articlesFromStorage.size())
                .forEach(index -> {
                    ArticleResponseDto article = ArticleResponseDto.builder().build();
                    article.setArticleId(articlesFromStorage.get(index).getArticleId());
                    article.setTitle(articlesFromStorage.get(index).getTitle());
                    article.setYearPublished(articlesFromStorage.get(index).getYearPublished());
                    article.setAuthors(convertFromStringArrayToString(articlesFromStorage.get(index).getAuthors()));
                    article.setStatus(articlesFromStorage.get(index).getStatus());
                    article.setKeywords(convertFromStringArrayToString(articlesFromStorage.get(index).getKeywords()));
                    String coverPageInBase64 = convertFromByteArrayToBase64(articlesFromStorage.get(index).getCoverPageImage());
                    article.setCoverPage(coverPageInBase64);
//                    article.setCoverPage(articlesFromStorage.get(index).getCoverPageImage());
                    String articleInBase64 = convertFromByteArrayToBase64(articlesFromStorage.get(index).getArticlePdf());
                    article.setArticlePdf(articleInBase64);
//                    article.setArticlePdf(articlesFromStorage.get(index).getArticlePdf());
                    article.setAbstractDescription(articlesFromStorage.get(index).getAbstractDescription());
                    article.setAcademicJournal(articlesFromStorage.get(index).getAcademicJournal());
                    article.setFieldOfScience(articlesFromStorage.get(index).getFieldOfScience());
                    article.setCreator(articlesFromStorage.get(index).getCreator());

                    articlesResponse.add(article);
                });

        return articlesResponse;
    }

    //TODO check this later
    private static byte[] saveArticleAsByteArray(String articlePath) {
        try{
//            BufferedImage image = ImageIO.read(new File(articlePath));
//            ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
//            ImageIO.write(image, "jpg", outStreamObj);
            byte[] bytes = Files.readAllBytes(Paths.get(articlePath));
//            return outStreamObj.toByteArray();
            return bytes;
        } catch (IOException ioex) {
            System.out.println("IO Exception occurred while writing image in byte array.");
//            ioex.printStackTrace();
        }
        System.out.println("Here");
        return new byte[]{};
    }

    private static String convertFromByteArrayToBase64(byte[] image) {
        byte[] enteredString = Base64.getEncoder().encode(image);
        byte[] decodedString = new byte[0];
        try {
            decodedString = Base64.getDecoder().decode(new String(enteredString).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException occurred while trying to convert base64 string to byte array.");
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "ERRXXX", "UnsupportedEncodingException occurred while trying to convert base64 string to byte array.");
        }
        return new String(decodedString);
    }

    private static byte[] convertFromBase64StringToByteArray(String base64String) {
        byte[] enteredString = Base64.getEncoder().encode(base64String.getBytes());
        byte[] decodedString = new byte[0];
        try {
            decodedString = Base64.getDecoder().decode(new String(enteredString).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException occurred while trying to convert base64 string to byte array.");
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "ERRXXX", "UnsupportedEncodingException occurred while trying to convert base64 string to byte array.");
        }
        return decodedString;
    }

    public List<ArticleResponseDto> getAllArticlesByArticleStatus(String status) {
        List<Article> articlesWithStatus = articleRepository.findAll()
                .stream()
                .filter(article -> status.equalsIgnoreCase(article.getStatus()))
                .toList();
        List<ArticleResponseDto> resultArticlesList = new ArrayList<>();

        if(articlesWithStatus.size() == 0){
            throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, "SOME_ERROR_CODE", String.format("There are no articles with status: %s", status));
        }

        IntStream.range(0, articlesWithStatus.size())
                .forEach(index -> {
                    resultArticlesList.add(setArticleResponseDto(Optional.ofNullable(articlesWithStatus.get(index))));
                });

        return resultArticlesList;
    }

    public List<ArticleResponseDto> getArticleByKeywords(String keyword) {

        List<ArticleResponseDto> resultList = new ArrayList<>();
        List<Article> articles = articleRepository.findAll();
        //TODO This search is better to be done in a select in database
        // I have selected to do it here to demonstrate java code
        // ALso it is better to be rewritten in java 8+ standard
        for (Article article : articles) {
            for (String word : article.getKeywords()) {
                if(word.toLowerCase().contains(keyword.toLowerCase())) {
                    resultList.add(setArticleResponseDto(Optional.of(article)));
                }
            }
        }

//        articles.forEach(article -> {
//            Arrays.stream(article.getKeywords()).toList()
//                    .stream().filter(word -> word.toLowerCase().contains(keyword.toLowerCase()))
//                    .collect(Collectors.toList());
//        });
//
//        resultList = articles.stream()
//                .filter(article -> Arrays.stream(article.getKeywords())
//                        .filter(word -> ))

        if(resultList.size() == 0) {
            throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, "SOME_ERROR_CODE", String.format("There are no articles that contain keyword: %s", keyword));
        }

        return resultList;
    }

    public List<ArticleResponseDto> getArticleByFieldOfScience(String science) {
        List<Article> articles = articleRepository.findAll()
                .stream()
                .filter(article -> article.getFieldOfScience().toLowerCase().contains(science.toLowerCase()))
                .toList();

        List<ArticleResponseDto> resultArticles = new ArrayList<>();

        if(articles.size() == 0) {
            throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, "SOME_ERROR_CODE", String.format("There are no articles with field of science: %s", science));
        }

        IntStream.range(0, articles.size())
                .forEach(index -> {
                    resultArticles.add(setArticleResponseDto(Optional.ofNullable(articles.get(index))));
                });

        return resultArticles;
    }

    public List<ArticleResponseDto> getArticlesForUser(String username) {
        List<Article> articles = articleRepository.findAll()
                .stream()
                .filter(article -> article.getCreator().toLowerCase().contains(username.toLowerCase()))
                .toList();

        List<ArticleResponseDto> resultArticles = new ArrayList<>();

        if(articles.size() == 0) {
            throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, "SOME_ERROR_CODE", String.format("There are no articles created by: %s", username));
        }

        IntStream.range(0, articles.size())
                .forEach(index -> {
                    resultArticles.add(setArticleResponseDto(Optional.ofNullable(articles.get(index))));
                });

        return resultArticles;
    }
}
