package com.codelikealexito.articles.api.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountReferencesResponseDto {
    private long count;
}
