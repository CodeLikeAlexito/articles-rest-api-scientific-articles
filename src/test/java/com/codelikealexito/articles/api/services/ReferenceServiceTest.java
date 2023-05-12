package com.codelikealexito.articles.api.services;

import com.codelikealexito.articles.api.dtos.CountReferencesResponseDto;
import com.codelikealexito.articles.api.dtos.ReferenceDto;
import com.codelikealexito.articles.api.entites.Reference;
import com.codelikealexito.articles.api.repositories.ReferenceRepository;
import com.codelikealexito.articles.api.util.ReferenceHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceServiceTest {

    @Mock
    private ReferenceRepository referenceRepository;
    @InjectMocks
    private ReferenceService referenceService;

    private static final Long ARTICLE_ID = 1L;
    private static final String REFERENCE_TEXT = "Reference text";
    private static final String ARTICLE_TITLE = "test1";

    @Test
    public void testGetAllReferences() {
        List<ReferenceDto> expectedReferences = new ArrayList<>();
        ReferenceDto referenceDto = new ReferenceDto();
        referenceDto.setArticleId(ARTICLE_ID);
        referenceDto.setReferenceText(REFERENCE_TEXT);
        expectedReferences.add(referenceDto);

        // Mock the helper method
        when(ReferenceHelper.getAllReferencesFromJsonFile()).thenReturn(expectedReferences);

        List<ReferenceDto> actualReferences = referenceService.getAllReferences();

        Assert.assertEquals(expectedReferences, actualReferences);
    }

    @Test
    public void testGetAllReferencesForArticle() {
        List<Reference> references = new ArrayList<>();
        Reference reference = Mocks.createMockReference();
        references.add(reference);

        // Mock the repository method
        when(referenceRepository.findAll()).thenReturn(references);

        List<ReferenceDto> expectedReferences = new ArrayList<>();
        ReferenceDto referenceDto = new ReferenceDto();
        referenceDto.setArticleId(ARTICLE_ID);
        referenceDto.setReferenceText(REFERENCE_TEXT);
        expectedReferences.add(referenceDto);

        List<ReferenceDto> actualReferences = referenceService.getAllReferencesForArticle(ARTICLE_ID);

        Assert.assertEquals(expectedReferences, actualReferences);
    }

    @Test
    public void testFillDatabaseEntityWithData() {
        List<ReferenceDto> referencesList = new ArrayList<>();
        ReferenceDto referenceDto = new ReferenceDto();
        referenceDto.setArticleId(ARTICLE_ID);
        referenceDto.setReferenceText(REFERENCE_TEXT);
        referencesList.add(referenceDto);

        // Mock the helper method
        when(ReferenceHelper.getAllReferencesFromJsonFile()).thenReturn(referencesList);

        Map<String, Boolean> expectedHashMap = new HashMap<>();
        expectedHashMap.put("inserted", true);

        Map<String, Boolean> actualHashMap = referenceService.fillDatabaseEntityWithData();

        Assert.assertEquals(expectedHashMap, actualHashMap);

        // Verify that the repository method was called the expected number of times
        Mockito.verify(referenceRepository, Mockito.times(referencesList.size())).save(Mockito.any(Reference.class));
    }

    @Test
    public void testCountNumberOfCitedArticle() {
        List<Reference> references = new ArrayList<>();
        Reference reference = Mocks.createMockReference();
        references.add(reference);

        // Mock the repository method
        when(referenceRepository.findAll()).thenReturn(references);

        Long expectedCount = 1L;
        Long actualCount = referenceService.countNumberOfCitedArticle(ARTICLE_TITLE);

        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testCountedNumberOfCitedArticle() {
        Long expectedCount = 1L;

        // Mock the service method
        when(referenceService.countNumberOfCitedArticle(ARTICLE_TITLE)).thenReturn(expectedCount);

        CountReferencesResponseDto expectedResponseDto = CountReferencesResponseDto.builder().count(expectedCount).build();
        CountReferencesResponseDto actualResponseDto = referenceService.countedNumberOfCitedArticle(ARTICLE_TITLE);

        Assert.assertEquals(expectedResponseDto, actualResponseDto);
    }
}
