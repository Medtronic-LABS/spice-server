package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.ModelQuestions;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.ModelQuestionsRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.ModelQuestionsServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Model Questions Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ModelQuestionsServiceTest {

    @InjectMocks
    private ModelQuestionsServiceImpl modelQuestionsService;

    @Mock
    private ModelQuestionsRepository modelQuestionsRepository;

    @Test
    void getModelQuestions() {
        //given
        List<ModelQuestions> modelQuestions = List.of(TestDataProvider.getModelQuestions());

        //when
        when(modelQuestionsRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(modelQuestions);

        //then
        List<ModelQuestions> questionsList = modelQuestionsService.getModelQuestions();
        Assertions.assertEquals(modelQuestions.size(), questionsList.size());

        //then
        questionsList = modelQuestionsService.getModelQuestions();
        Assertions.assertFalse(Constants.MODEL_QUESTIONS.isEmpty());
    }

    @Test
    void testGetModelQuestions1() {
        //given
        List<ModelQuestions> modelQuestions = List.of(TestDataProvider.getModelQuestions());

        //when
        when(modelQuestionsRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(modelQuestions);

        //then
        List<ModelQuestions> modelQuestionsList = modelQuestionsService.getModelQuestions(1L);
        Assertions.assertNotNull(modelQuestionsList);
    }

    @Test
    void getModelQuestionsByIsDefault() {
        //given
        List<ModelQuestions> modelQuestions = List.of(TestDataProvider.getModelQuestions());

        //when
        when(modelQuestionsRepository.findByIsDefaultAndIsDeleted(Boolean.TRUE,
                Boolean.FALSE)).thenReturn(modelQuestions);

        //then
        List<ModelQuestions> modelQuestionsList = modelQuestionsService.getModelQuestionsByIsDefault();
        Assertions.assertNotNull(modelQuestionsList);
    }
}