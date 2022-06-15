package com.codelikealexito.articles.api.services;

import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.dtos.ArticleResponseDto;
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
import java.util.stream.IntStream;

import java.util.Base64;

@Service
public class ArticleService {

    public List<Article> getAllBooks() {
        return getHardCodedBooks();
    }

    public Article getBookByTitle(String title) {
        List<Article> allBooks = getHardCodedBooks();
        return allBooks.stream()
                .filter(book -> title.equalsIgnoreCase(book.getTitle()))
                .findAny()
                .orElse(null);
    }
    ///home/alex/IdeaProjects/mkd-cars/books-api/src/main/java/com/codelikealexito/books/api/images/Book3.jpeg
    private static List<Article> getHardCodedBooks(){
        Article book1 = new Article(1L, "Book1", "Author1", "Genre1", "1900", saveImageAsByteArray("/home/alex/IdeaProjects/mkd-cars/books-api/src/main/java/com/codelikealexito/books/api/images/Book1.jpg"));
        Article book2 = new Article(2L, "Book2", "Author2", "Genre2", "1902", saveImageAsByteArray("/home/alex/IdeaProjects/mkd-cars/books-api/src/main/java/com/codelikealexito/books/api/images/Book2.jpg"));
        Article book3 = new Article(3L, "Book3", "Author3", "Genre3", "1903", saveImageAsByteArray("/home/alex/IdeaProjects/mkd-cars/books-api/src/main/java/com/codelikealexito/books/api/images/Book3.jpeg"));
        Article book4 = new Article(4L, "Book4", "Author4", "Genre4", "1904", saveImageAsByteArray("/home/alex/IdeaProjects/mkd-cars/books-api/src/main/java/com/codelikealexito/books/api/images/Book4.jpeg"));
        Article book5 = new Article(5L, "Book5", "Author5", "Genre5", "1905", saveImageAsByteArray("/home/alex/IdeaProjects/mkd-cars/books-api/src/main/java/com/codelikealexito/books/api/images/Book5.jpg"));

        List<Article> returnList = new ArrayList<>();
        returnList.add(book1);
        returnList.add(book2);
        returnList.add(book3);
        returnList.add(book4);
        returnList.add(book5);

        return returnList;
    }

    private List<ArticleResponseDto> getAllBooksFinalObject() {
        List<Article> booksFromStorage = getHardCodedBooks();
        List<ArticleResponseDto> booksResponse = new ArrayList<>();

        IntStream.range(0, booksFromStorage.size())
                .forEach(index -> {
                    ArticleResponseDto book = new ArticleResponseDto();
                    book.setBookId(booksFromStorage.get(index).getBookId());
                    book.setTitle(booksFromStorage.get(index).getTitle());
                    book.setAuthor(booksFromStorage.get(index).getAuthor());
                    book.setGenre(booksFromStorage.get(index).getGenre());
                    book.setYear(booksFromStorage.get(index).getYear());
                    String imageInBase64 = convertImageFromByteArrayToBase64(booksFromStorage.get(index).getImage());
                    book.setImageAsBase64(imageInBase64);

                    booksResponse.add(book);
                });

        return booksResponse;
    }

    private static byte[] saveImageAsByteArray(String imagePath) {
        try{
//            BufferedImage image = ImageIO.read(new File(imagePath));
//            ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
//            ImageIO.write(image, "jpg", outStreamObj);
            byte[] bytes = Files.readAllBytes(Paths.get(imagePath));
//            return outStreamObj.toByteArray();
            return bytes;
        } catch (IOException ioex) {
            System.out.println("IO Exception occurred while writing image in byte array.");
//            ioex.printStackTrace();
        }
        System.out.println("Here");
        return new byte[]{};
    }

    private static String convertImageFromByteArrayToBase64(byte[] image) {
//        String result = Base64Utils.encodeToString(image);
        String result = Base64.getEncoder().encodeToString(image);
//        System.out.println(result);
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
}
