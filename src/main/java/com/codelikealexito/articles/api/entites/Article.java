package com.codelikealexito.articles.api.entites;


import com.codelikealexito.articles.api.enums.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
//TODO add shortDescription, description in db ( check if something else is missing as business field )
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;
    @Column
    private String title;
    @Column(name = "year_published")
    private String yearPublished;
    /* Should contain at least 1 author. Can have more than 1 also. */
    @Column
    private String[] authors;
    @Column(name = "cover_page")
    @Lob
    private byte[] coverPageImage;
    @Column(name = "article_pdf")
    @Lob
    private byte[] articlePdf;

    @Column(name = "abstract_description")
    private String abstractDescription;
    @Column(name = "academic_journal")
    private String academicJournal;
    @Column(name = "field_of_science")
    private String fieldOfScience;

    private String status;

    @Column(name = "creator")
    private String creator;

    public static Article createArticle(Long articleId, String title, String yearPublished, String[] authors, byte[] coverPageImage, byte[] articlePdf,
                                        String abstractDescription, String academicJournal, String fieldOfScience, Status status, String creator){
        return new Article(articleId, title, yearPublished, authors, coverPageImage, articlePdf, abstractDescription, academicJournal, fieldOfScience, status.name(), creator);
    }

    public static Article updateArticle(Long articleId, String title, String yearPublished, String[] authors, byte[] coverPageImage, byte[] articlePdf,
                                        String abstractDescription, String academicJournal, String fieldOfScience, Status status, String creator){
        return new Article(null, title, yearPublished, authors, coverPageImage, articlePdf, abstractDescription, academicJournal, fieldOfScience, status.name(), creator);
    }
}
