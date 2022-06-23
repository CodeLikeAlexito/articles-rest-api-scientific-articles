package com.codelikealexito.articles.api.services;

import com.codelikealexito.articles.api.dtos.ArticleRequestDto;
import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.dtos.ArticleResponseDto;
import com.codelikealexito.articles.api.repositories.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.Base64;

@Service
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     *
     * @return
     */
    public ResponseEntity<List<ArticleResponseDto>> getAllBooks() {
        return ResponseEntity.ok(getAllBooksFinalObject());
    }

    /**
     *
     * @param title
     * @return
     */
    public List<ArticleResponseDto> getArticleByTitle(String title) {
        List<ArticleResponseDto> allBooks = getAllBooksFinalObject();
        return allBooks.stream()
                .filter(book -> title.equalsIgnoreCase(book.getTitle()))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id
     * @return
     */
    public ArticleResponseDto getArticleById(Long id) throws Exception {
        List<ArticleResponseDto> allBooks = getAllBooksFinalObject();
        return allBooks.stream()
                .filter(book -> id == book.getArticleId())
                .findAny()
                .orElseThrow(() -> new Exception(String.format("Article with id: %d is not found", id)));
    }

    public List<Article> getAllArticlesFromDb() {
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
    private List<ArticleResponseDto> getAllBooksFinalObject() {
        List<Article> articlesFromStorage = getAllArticlesFromDb();
        List<ArticleResponseDto> articlesResponse = new ArrayList<>();

        IntStream.range(0, articlesFromStorage.size())
                .forEach(index -> {
                    ArticleResponseDto article = new ArticleResponseDto();
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


    public Article addArticle(ArticleRequestDto articleRequestDto) {
        return null;
    }
}
