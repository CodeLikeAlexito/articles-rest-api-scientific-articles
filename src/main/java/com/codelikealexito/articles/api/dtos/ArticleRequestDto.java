package com.codelikealexito.articles.api.dtos;

import com.codelikealexito.articles.api.enums.Status;
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
    private String[] authors;
    private byte[] coverPageImage;
    private byte[] articlePdf;

    private String abstractDescription;
    private String academicJournal;
    private String fieldOfScience;

//    private Status status;

    private String creator;
}
