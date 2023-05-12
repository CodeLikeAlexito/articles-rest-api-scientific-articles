package com.codelikealexito.articles.api.services;

import com.codelikealexito.articles.api.dtos.ArticleRequestDto;
import com.codelikealexito.articles.api.dtos.ArticleResponseDTO;
import com.codelikealexito.articles.api.entites.Article;
import com.codelikealexito.articles.api.entites.Reference;
import com.codelikealexito.articles.api.enums.Status;

import java.util.ArrayList;
import java.util.List;

public class Mocks {

    public static Article mockArticle() {
        return Article.createArticle(
                null,
                "Title",
                "mockYearPublished1",
                new String[]{"mockAuthor1", ",mockAuthor2"},
                new String[] {"mockKeyword1", "mockKeyword2"},
                null,
                null,
                "mockAbstractDescription",
                "mockAcademicJournal",
                "mockFieldOfScience",
                Status.APPROVED,
                "mockCreator"
        );
    }

    public static Article createMockArticleChatGpt() {
        return Article.createArticle(
                null,
                "Mock Article Title",
                "2022",
                new String[] {"John Doe"},
                new String[] {"Mock", "Article", "Keywords"},
                "mock_cover_page_base64_string".getBytes(),
                "mock_article_pdf_base64_string".getBytes(),
                "Mock abstract description.",
                "Mock Academic Journal",
                "Mock Field of Science",
                Status.APPROVED,
                "Mock Creator"
        );
    }


    public static ArticleResponseDTO createMockArticleResponseDTO() {
        return new ArticleResponseDTO(
                null,
                "Mock Article Title",
                "2022",
                "John Doe",
                "Mock, Article, Keywords",
                "mock_cover_page_base64_string",
                "mock_article_pdf_base64_string",
                "Mock abstract description.",
                "Mock Academic Journal",
                "Mock Field of Science",
                "APPROVED",
                "Mock Creator"
        );
    }

    public static ArticleRequestDto createMockArticleRequestDto() {
        return new ArticleRequestDto(
                null,
                "Mock Article Title",
                "2022",
                "John Doe",
                "Mock, Article, Keywords",
                "mock_cover_page_base64_string",
                "mock_article_pdf_base64_string",
                "Mock abstract description.",
                "Mock Academic Journal",
                "Mock Field of Science",
                "APPROVED",
                "Mock Creator"
        );
    }

    public static List<ArticleResponseDTO> createMockArticleResponseDtoList() {
        List<ArticleResponseDTO> articleList = new ArrayList<>();

        // add the first mock ArticleResponseDTO object to the list
        articleList.add(new ArticleResponseDTO(
                1L,
                "Mock Article Title 1",
                "2022",
                "John Doe",
                "Mock, Article, Keywords",
                "mock_cover_page_1_base64_string",
                "mock_article_pdf_1_base64_string",
                "Mock abstract description 1.",
                "Mock Academic Journal 1",
                "Mock Field of Science 1",
                "PUBLISHED",
                "Mock Creator 1"
        ));

        // add the second mock ArticleResponseDTO object to the list
        articleList.add(new ArticleResponseDTO(
                2L,
                "Mock Article Title 2",
                "2023",
                "Jane Doe",
                "Mock, Article, Keywords",
                "mock_cover_page_2_base64_string",
                "mock_article_pdf_2_base64_string",
                "Mock abstract description 2.",
                "Mock Academic Journal 2",
                "Mock Field of Science 2",
                "DRAFT",
                "Mock Creator 2"
        ));

        return articleList;
    }

    public static Reference createMockReference() {
        return Reference.createReference(1L, 2L, "Mock Reference");
    }


}
