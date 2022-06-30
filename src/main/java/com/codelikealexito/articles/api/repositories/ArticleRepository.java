package com.codelikealexito.articles.api.repositories;

import com.codelikealexito.articles.api.entites.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitle(String title);

//    @Query(value = "select * from Article a where a.keywords like (%:keyword%)", nativeQuery=true)
//    List<Article> findArticlesByKeywords(@Param("keyword") String keyword);
}
