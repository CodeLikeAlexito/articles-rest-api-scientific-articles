package com.codelikealexito.articles.api.entites;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;
    @Column
    private String title;
    @Column
    private String genre;
    @Column
    private String year;
    /* Should contain at least 1 author. Can have more than 1 also. */
    @Column
    private String[] authors;
    @Column(name = "article_pdf")
    private byte[] articlePdf;

    @Column(name = "creator")
    private String creator;

    public static Article createArticle(Long articleId, String title, String genre, String year, String[] authors, byte[] articlePdf, String creator){
        return new Article(articleId, title, genre, year, authors, articlePdf, creator);
    }

    public static Article editArticle(Long articleId, String title, String genre, String year, String[] authors, byte[] articlePdf, String creator){
        return new Article(null, title, genre, year, authors, articlePdf, creator);
    }
}
