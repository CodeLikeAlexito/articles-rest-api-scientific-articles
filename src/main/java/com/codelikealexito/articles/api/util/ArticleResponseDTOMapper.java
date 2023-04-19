package com.codelikealexito.articles.api.util;

import com.codelikealexito.articles.api.dtos.ArticleResponseDTO;
import com.codelikealexito.articles.api.entites.Article;
import org.springframework.stereotype.Service;
import java.util.function.Function;

import static com.codelikealexito.articles.api.util.Helper.*;

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
}
