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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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
        List<Article> articles =  articleRepository.findByTitle(title);
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
        //TODO this values needs to come from React FE. For the moment they are hardcoded.
        byte[] articleAsByteArray = saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/pdf/an-efficient-multi-objective-meta-heuristic-method-for-probabilistic-transmission-network-planning.pdf");
        byte[] articleCoverAsByteArray = saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/images/Book1.jpg");

        Article article = Article.createArticle(articleRequestDto.getArticleId(), articleRequestDto.getTitle(), articleRequestDto.getYearPublished(), articleRequestDto.getAuthors()
                , articleCoverAsByteArray, articleAsByteArray, articleRequestDto.getAbstractDescription()
                , articleRequestDto.getAcademicJournal(), articleRequestDto.getFieldOfScience(), Status.PENDING, articleRequestDto.getCreator());
        return articleRepository.save(article);
    }

    public Article editArticle(ArticleRequestDto articleRequestDto) throws Exception {
        Article currentArticle = articleRepository.findById(articleRequestDto.getArticleId())
                .orElseThrow(() ->  new Exception("Article does not exists. Article id: " + articleRequestDto.getArticleId()));

        Article article = Article.createArticle(currentArticle.getArticleId(), articleRequestDto.getTitle(), articleRequestDto.getYearPublished(), articleRequestDto.getAuthors()
                , articleRequestDto.getCoverPageImage(), articleRequestDto.getArticlePdf(), articleRequestDto.getAbstractDescription()
                , articleRequestDto.getAcademicJournal(), articleRequestDto.getFieldOfScience(), Status.PENDING, articleRequestDto.getCreator());
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
                .authors(article.get().getAuthors())
                .coverPage(convertFromByteArrayToBase64(article.get().getCoverPageImage()))
                .articlePdf(convertFromByteArrayToBase64(article.get().getArticlePdf()))
                .abstractDescription(article.get().getAbstractDescription())
                .academicJournal(article.get().getAcademicJournal())
                .fieldOfScience(article.get().getFieldOfScience())
                .creator(article.get().getCreator())
                .build();
    }

    private List<Article> getAllArticlesFromDb() {
        return articleRepository.findAll();
    }

    ///home/alex/IdeaProjects/mkd-cars/books-api/src/main/java/com/codelikealexito/books/api/images/Book3.jpeg
//    private static List<Article> getHardCodedBooks(){
//        Article article1 = Article.createArticle(1L, "Article1", "Genre1", "1900", new String[]{"Aleks"}, saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/Book1.jpg"), "codeliekalex");
//        Article article2 = Article.createArticle(2L, "Article2", "Genre2", "1900", new String[]{"Aleks2"}, saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/Book2.jpg"), "codeliekalex");
//        Article article3 = Article.createArticle(3L, "Article3", "Genre3", "1900", new String[]{"Aleks3"}, saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/Book3.jpg"), "codeliekalex");
//        Article article4 = Article.createArticle(4L, "Article4", "Genre4", "1900", new String[]{"Aleks4"}, saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/Book4.jpg"), "codeliekalex");
//        Article article5 = Article.createArticle(5L, "Article5", "Genre5", "1900", new String[]{"Aleks5"}, saveArticleAsByteArray("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/java/com/codelikealexito/articles/api/files/Book5.jpg"), "codeliekalex");
//
//        List<Article> returnList = new ArrayList<>();
//        returnList.add(article1);
//        returnList.add(article2);
//        returnList.add(article3);
//        returnList.add(article4);
//        returnList.add(article5);
//
//        return returnList;
//    }

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
                    article.setAuthors(articlesFromStorage.get(index).getAuthors());
                    String coverPageInBase64 = convertFromByteArrayToBase64(articlesFromStorage.get(index).getCoverPageImage());
                    article.setCoverPage(coverPageInBase64);
                    String articleInBase64 = convertFromByteArrayToBase64(articlesFromStorage.get(index).getArticlePdf());
                    article.setArticlePdf(articleInBase64);
                    article.setAbstractDescription(articlesFromStorage.get(index).getAbstractDescription());
                    article.setAcademicJournal(articlesFromStorage.get(index).getAcademicJournal());
                    article.setFieldOfScience(articlesFromStorage.get(index).getFieldOfScience());
                    article.setCreator(articlesFromStorage.get(index).getCreator());

                    articlesResponse.add(article);
                });

        return articlesResponse;
    }

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
        String result = Base64.getEncoder().encodeToString(image);
        return result;
    }

    // To be deleted later
    private void test() throws IOException {
        // read the image from the file
        BufferedImage image = ImageIO.read(new File("/home/alex/IdeaProjects/mkd-cars/books-api/src/main/java/com/codelikealexito/books/api/images/Book1.jpg"));

        // create the object of ByteArrayOutputStream class
        ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();

        // write the image into the object of ByteArrayOutputStream class
        ImageIO.write(image, "jpg", outStreamObj);

        // create the byte array from image
        byte [] byteArray = outStreamObj.toByteArray();
        System.out.println(byteArray);

        // create the object of ByteArrayInputStream class
        // and initialized it with the byte array.
        ByteArrayInputStream inStreambj = new ByteArrayInputStream(byteArray);

        // read image from byte array
        BufferedImage newImage = ImageIO.read(inStreambj);

        // write output image
        ImageIO.write(newImage, "jpg", new File("outputImage.jpg"));
        System.out.println("Image generated from the byte array.");
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
}
