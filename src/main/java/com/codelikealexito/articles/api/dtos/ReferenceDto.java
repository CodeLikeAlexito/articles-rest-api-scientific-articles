package com.codelikealexito.articles.api.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReferenceDto {

    @JsonProperty("article_id")
    private Long articleId;
    @JsonProperty("reference_text")
    private String referenceText;

}
