package com.codelikealexito.articles.api.util;

import com.codelikealexito.articles.api.dtos.ArticleResponseDTO;
import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.exceptions.CustomResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.function.Function;

@Service
public class ArticleResponseDTOMapper implements Function<Article, ArticleResponseDTO> {
    @Override
    public ArticleResponseDTO apply(Article article) {

        return new ArticleResponseDTO(
                article.getArticleId(),
                article.getTitle(),
                article.getYearPublished(),
                convertFromStringArrayToString(article.getAuthors()),
                convertFromStringArrayToString(article.getKeywords()),
                convertFromByteArrayToBase64(article.getCoverPageImage()),
                convertFromByteArrayToBase64(article.getArticlePdf()),
                article.getAbstractDescription(),
                article.getAcademicJournal(),
                article.getFieldOfScience(),
                article.getCreator(),
                article.getStatus()
        );
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

    private static String convertFromByteArrayToBase64(byte[] image) {
        byte[] enteredString = Base64.getEncoder().encode(image);
        byte[] decodedString = new byte[0];
        try {
            decodedString = Base64.getDecoder().decode(new String(enteredString).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
//            log.error("UnsupportedEncodingException occurred while trying to convert base64 string to byte array.");
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "ERRXXX", "UnsupportedEncodingException occurred while trying to convert base64 string to byte array.");
        }
        return new String(decodedString);
    }
}
