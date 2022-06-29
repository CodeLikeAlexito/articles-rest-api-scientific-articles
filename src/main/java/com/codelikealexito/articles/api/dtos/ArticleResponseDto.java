package com.codelikealexito.articles.api.dtos;

import com.codelikealexito.articles.api.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleResponseDto {
    private Long articleId;
    private String title;

    private String yearPublished;
    private String[] authors;
    private String[] keywords;
    private String coverPage; //base64 String

    private String articlePdf; //base64 String

    private String abstractDescription;
    private String academicJournal;
    private String fieldOfScience;

    private String status;

    // client/author username from client table that created/posted the article
    private String creator;
}
