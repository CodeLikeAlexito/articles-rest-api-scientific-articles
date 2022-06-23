package com.codelikealexito.articles.api.dtos;

import lombok.Data;

@Data
public class ArticleResponseDto {
    private Long articleId;
    private String title;
    private String genre;
    private String year;
    private String[] authors;
    private String coverPage;
    private String creator;
}
