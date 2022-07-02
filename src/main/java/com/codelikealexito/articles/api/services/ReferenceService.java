package com.codelikealexito.articles.api.services;

import com.codelikealexito.articles.api.dtos.ReferenceDto;
import com.codelikealexito.articles.api.entites.Reference;
import com.codelikealexito.articles.api.exceptions.CustomResponseStatusException;
import com.codelikealexito.articles.api.repositories.ReferenceRepository;
import com.codelikealexito.articles.api.util.ReferenceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
                .filter(reference -> reference.getArticleId() == articleId)
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

        if(referencesList.size() == 0) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "SOME_ERR", "Refernce service fill reference table with data error.");
        }

        IntStream.range(0, referencesList.size())
                .forEach(index -> {
                    referenceRepository.save(Reference.createReference(null, referencesList.get(index).getArticleId(), referencesList.get(index).getReferenceText()));
                });

        Map<String, Boolean> resultHashMap = new HashMap<>();
        resultHashMap.put("instered", true);

        return resultHashMap;
    }

    public Long countNumberOfCitedArticle(String articleTitle) {
        return referenceRepository.findAll()
                .stream()
                .filter(reference -> reference.getReference().toLowerCase().contains(articleTitle.toLowerCase()))
                .count();
    }

    public Map<String, Long> countedNumberOfCitedArticle(String articleTitle) {
        Map<String, Long> result = new HashMap<>();
        Long count = countNumberOfCitedArticle(articleTitle);
        result.put("Count: ", count);
        return result;
    }

}
