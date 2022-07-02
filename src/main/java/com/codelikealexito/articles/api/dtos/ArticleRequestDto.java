package com.codelikealexito.articles.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequestDto {
    private Long articleId;
    private String title;
    private String yearPublished;
    private String authors;
    private String keywords;
    private String coverPageImage;
    private String articlePdf;

    private String abstractDescription;
    private String academicJournal;
    private String fieldOfScience;

    private String status;
    private String creator;
}
