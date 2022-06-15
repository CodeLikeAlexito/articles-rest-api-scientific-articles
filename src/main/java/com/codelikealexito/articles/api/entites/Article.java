package com.codelikealexito.articles.api.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Long bookId;
    private String title;
    private String author;
    private String genre;
    private String year;
    private byte[] image;
}
