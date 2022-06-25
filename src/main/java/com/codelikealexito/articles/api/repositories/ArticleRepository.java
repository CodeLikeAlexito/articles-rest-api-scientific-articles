package com.codelikealexito.articles.api.repositories;

import com.codelikealexito.articles.api.entites.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitle(String title);
}
