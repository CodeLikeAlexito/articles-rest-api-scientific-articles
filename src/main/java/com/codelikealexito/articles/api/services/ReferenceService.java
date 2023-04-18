package com.codelikealexito.articles.api.services;

import com.codelikealexito.articles.api.dtos.CountReferencesResponseDto;
import com.codelikealexito.articles.api.dtos.ReferenceDto;
import com.codelikealexito.articles.api.entites.Reference;
import com.codelikealexito.articles.api.exceptions.CustomResponseStatusException;
import com.codelikealexito.articles.api.repositories.ReferenceRepository;
import com.codelikealexito.articles.api.util.ReferenceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ReferenceService {

    private final ReferenceRepository referenceRepository;

    public ReferenceService(ReferenceRepository referenceRepository) {
        this.referenceRepository = referenceRepository;
    }

    public List<ReferenceDto> getAllReferences() {
        return ReferenceHelper.getAllReferencesFromJsonFile();
    }

    public List<ReferenceDto> getAllReferencesForArticle(Long articleId){
        List<Reference> allReferences = referenceRepository.findAll()
                .stream()
                .filter(reference -> articleId.equals(reference.getArticleId()) )
                .toList();

        List<ReferenceDto> listReferenceDto = new ArrayList<>();
        IntStream.range(0, allReferences.size())
                .forEach(index -> {
                    ReferenceDto referenceDto = new ReferenceDto();
                    referenceDto.setArticleId(allReferences.get(index).getArticleId());
                    referenceDto.setReferenceText(allReferences.get(index).getReference());
                    listReferenceDto.add(referenceDto);
                });

        return listReferenceDto;
    }

    public Map<String, Boolean> fillDatabaseEntityWithData() {

        List<ReferenceDto> referencesList = ReferenceHelper.getAllReferencesFromJsonFile();

        if(referencesList.isEmpty()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "SOME_ERR", "Reference service fill reference table with data error.");
        }

        IntStream.range(0, referencesList.size())
                .forEach(index -> {
                    referenceRepository.save(Reference.createReference(null, referencesList.get(index).getArticleId(), referencesList.get(index).getReferenceText()));
                });

        Map<String, Boolean> resultHashMap = new HashMap<>();
        resultHashMap.put("inserted", true);

        return resultHashMap;
    }

    public Long countNumberOfCitedArticle(String articleTitle) {
        return referenceRepository.findAll()
                .stream()
                .filter(reference -> reference.getReference().toLowerCase().contains(articleTitle.toLowerCase()))
                .count();
    }

    public CountReferencesResponseDto countedNumberOfCitedArticle(String articleTitle) {
        Long countRef = countNumberOfCitedArticle(articleTitle);

        return CountReferencesResponseDto
                .builder().count(countRef)
                .build();
    }

}
