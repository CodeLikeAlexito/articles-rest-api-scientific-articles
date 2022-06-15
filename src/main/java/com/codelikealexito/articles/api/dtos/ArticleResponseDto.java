package com.codelikealexito.articles.api.dtos;

import lombok.Data;

@Data
public class ArticleResponseDto {
    private Long bookId;
    private String title;
    private String author;
    private String genre;
    private String year;
    private String imageAsBase64;
}
