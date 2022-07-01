package com.codelikealexito.articles.api.util;

import com.codelikealexito.articles.api.dtos.ReferenceDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReferenceHelper {

    private static ObjectMapper mapper = new ObjectMapper();

    public static List<ReferenceDto> getAllReferencesFromJsonFile() {
        List<ReferenceDto> references = new ArrayList<>();
        try {
            references = mapper.readValue(new File("/home/alex/IdeaProjects/academic-articles-spring-api-reactjs/articles-api/src/main/resources/static/references.json"),
                    new TypeReference<List<ReferenceDto>>(){});
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return references;
    }
}
