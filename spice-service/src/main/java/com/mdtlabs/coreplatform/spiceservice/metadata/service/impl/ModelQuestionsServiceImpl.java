package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.ModelQuestions;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.ModelQuestionsRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ModelQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for ModelQuestions module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class ModelQuestionsServiceImpl extends GenericServiceImpl<ModelQuestions> implements ModelQuestionsService {

    @Autowired
    private ModelQuestionsRepository modelQuestionsRepository;

    /**
     * {@inheritDoc}
     */
    public List<ModelQuestions> getModelQuestions() {
        if (Constants.MODEL_QUESTIONS.isEmpty()) {
            Constants.MODEL_QUESTIONS.addAll(modelQuestionsRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.MODEL_QUESTIONS;
    }

    /**
     * {@inheritDoc}
     */
    public List<ModelQuestions> getModelQuestionsByIsDefault() {
        return modelQuestionsRepository.findByIsDefaultAndIsDeleted(Boolean.TRUE, Boolean.FALSE);
    }

    /**
     * {@inheritDoc}
     */
    public List<ModelQuestions> getModelQuestions(Long countryId) {
        return modelQuestionsRepository.findByCountryIdAndIsDeletedAndIsActiveOrderByDisplayOrderAsc(countryId, Boolean.FALSE, Boolean.TRUE);
    }

}
