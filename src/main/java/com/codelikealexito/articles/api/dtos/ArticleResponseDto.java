package com.codelikealexito.articles.api.dtos;

import lombok.Data;

@Data
public class ArticleResponseDto {
    private Long articleId;
    private String title;

    private String yearPublished;
    private String[] authors;
    private String coverPage;

    private String articlePdf; //base64 String

    private String abstractDescription;
    private String academicJournal;
    private String fieldOfScience;

    // client/author username from client table that created/posted the article
    private String creator;
}
