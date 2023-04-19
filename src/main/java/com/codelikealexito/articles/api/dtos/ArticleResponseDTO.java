package com.codelikealexito.articles.api.dtos;

public record ArticleResponseDTO(
        Long articleId,
        String title,
        String yearPublished,
        String authors,
        String keywords,
        String coverPage, //base64 String
        String articlePdf, //base64 String
        String abstractDescription,
        String academicJournal,
        String fieldOfScience,
        String status,
        String creator
) {

}
