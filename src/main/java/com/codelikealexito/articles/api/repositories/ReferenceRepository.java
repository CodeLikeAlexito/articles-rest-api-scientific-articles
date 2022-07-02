package com.codelikealexito.articles.api.repositories;

import com.codelikealexito.articles.api.entites.Reference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
}
